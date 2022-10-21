package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IBackupPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IBackupPreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.useCase.backup.GetBackupFileListUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupExportUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupImportUseCase
import sgtmelon.test.prod.RunPrivate
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult as Permission

class BackupPreferenceViewModel(
    callback: IBackupPreferenceFragment,
    private val getBackupFileList: GetBackupFileListUseCase,
    private val startBackupExport: StartBackupExportUseCase,
    private val startBackupImport: StartBackupImportUseCase
) : ParentViewModel<IBackupPreferenceFragment>(callback),
    IBackupPreferenceViewModel {

    override fun onSetup(bundle: Bundle?) {
        callback?.setup()

        val result = callback?.getStoragePermissionResult() ?: return

        val isAllowed = result == Permission.LOW_API || result == Permission.GRANTED

        if (!isAllowed) {
            callback?.updateExportEnabled(isEnabled = true)
            callback?.updateExportSummary(R.string.pref_summary_permission_need)
            callback?.updateImportEnabled(isEnabled = true)
            callback?.updateImportSummary(R.string.pref_summary_permission_need)
        } else {
            viewModelScope.launch { setupBackground() }
        }
    }

    @RunPrivate suspend fun setupBackground() {
        /**
         * Need to block export preference, otherwise user may come to coroutines circle.
         *
         * When save dialog dismiss -> onResume happen -> and it call [onSetup] -> and launch
         * the [setupBackground] coroutine (even if last one not ending).
         */
        callback?.updateExportEnabled(isEnabled = false)
        callback?.resetExportSummary()

        callback?.updateImportEnabled(isEnabled = false)
        callback?.startImportSummarySearch()
        val fileList = runBack { getBackupFileList() }
        callback?.stopImportSummarySearch()

        if (fileList.isEmpty()) {
            callback?.updateImportSummary(R.string.pref_summary_backup_import_empty)
        } else {
            callback?.updateImportSummaryFound(fileList.size)
            callback?.updateImportEnabled(isEnabled = true)
        }

        callback?.updateExportEnabled(isEnabled = true)
    }

    /**
     * Need reset list, because user can change permission or delete some files or
     * remove sd card. It calls even after permission dialog.
     */
    override fun onPause() {
        getBackupFileList.reset()
    }

    //region Export/import functions

    /**
     * This functions for check both permissions and if one of them is [Permission.FORBIDDEN]
     * need call [onClickExport] with that [Permission].
     */
    override fun onClickExport() {
        val storageResult = callback?.getStoragePermissionResult() ?: return

        if (storageResult == Permission.FORBIDDEN) {
            onClickExport(Permission.FORBIDDEN)
        } else {
            onClickExport(storageResult)
        }
    }

    /**
     * Call [startExport] only if [result] equals [Permission.LOW_API] or
     * [Permission.GRANTED]. Otherwise we must show dialog.
     */
    override fun onClickExport(result: Permission) {
        when (result) {
            Permission.ASK -> callback?.showExportPermissionDialog()
            Permission.LOW_API, Permission.GRANTED -> viewModelScope.launch { startExport() }
            Permission.FORBIDDEN -> callback?.showExportDenyDialog()
        }
    }

    @RunPrivate suspend fun startExport() {
        callback?.showExportLoadingDialog()
        val result = runBack { startBackupExport() }
        callback?.hideExportLoadingDialog()

        when (result) {
            is ExportResult.Success -> {
                callback?.showExportPathToast(result.path)

                /**
                 * Need update file list for feature import.
                 */
                getBackupFileList.reset()
                setupBackground()
            }
            is ExportResult.Error -> {
                callback?.showToast(R.string.pref_toast_export_error)
            }
        }
    }


    /**
     * This functions for check both permissions and if one of them is [Permission.FORBIDDEN]
     * need call [onClickImport] with that [Permission].
     */
    override fun onClickImport() {
        val storageResult = callback?.getStoragePermissionResult() ?: return

        if (storageResult == Permission.FORBIDDEN) {
            onClickImport(Permission.FORBIDDEN)
        } else {
            onClickImport(storageResult)
        }
    }

    /**
     * Call [prepareImportDialog] only if [result] equals [Permission.LOW_API] or
     * [Permission.GRANTED]. Otherwise we must show dialog.
     */
    override fun onClickImport(result: Permission) {
        when (result) {
            Permission.ASK -> callback?.showImportPermissionDialog()
            Permission.LOW_API, Permission.GRANTED -> viewModelScope.launch {
                prepareImportDialog()
            }
            Permission.FORBIDDEN -> callback?.showImportDenyDialog()
        }
    }

    @RunPrivate suspend fun prepareImportDialog() {
        val fileList = runBack { getBackupFileList() }
        val titleArray = fileList.map { it.name }.toTypedArray()

        if (titleArray.isEmpty()) {
            callback?.updateImportSummary(R.string.pref_summary_backup_import_empty)
            callback?.updateImportEnabled(isEnabled = false)
        } else {
            callback?.showImportDialog(titleArray)
        }
    }

    override fun onResultImport(name: String) {
        viewModelScope.launch {
            callback?.showImportLoadingDialog()
            val result = runBack { startBackupImport(name, getBackupFileList()) }
            callback?.hideImportLoadingDialog()

            when (result) {
                is ImportResult.Simple -> callback?.showToast(R.string.pref_toast_import_result)
                is ImportResult.Skip -> callback?.showImportSkipToast(result.skipCount)
                is ImportResult.Error -> callback?.showToast(R.string.pref_toast_import_error)
            }

            if (result == ImportResult.Error) return@launch

            callback?.sendTidyUpAlarmBroadcast()
            callback?.sendNotifyNotesBroadcast()
            callback?.sendNotifyInfoBroadcast()
        }
    }

    //endregion

}