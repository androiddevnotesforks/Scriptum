package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.IBackupPrefInteractor
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.extension.runBack
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IBackupPrefFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IBackupPrefViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [IBackupPrefFragment].
 */
class BackupPrefViewModel(
    application: Application
) : ParentViewModel<IBackupPrefFragment>(application),
    IBackupPrefViewModel {

    private lateinit var interactor: IBackupPrefInteractor

    fun setInteractor(interactor: IBackupPrefInteractor) {
        this.interactor = interactor
    }


    override fun onSetup(bundle: Bundle?) {
        callback?.setup()

        /**
         * Make import permission not enabled before [setupBackground] load data.
         */
        callback?.updateExportEnabled(isEnabled = false)
        callback?.updateImportEnabled(isEnabled = false)

        viewModelScope.launch { setupBackground() }
    }

    @RunPrivate suspend fun setupBackground() {
        val fileList = runBack { interactor.getFileList() }

        callback?.updateExportEnabled(isEnabled = true)

        if (fileList.isEmpty()) return

        callback?.updateImportEnabled(isEnabled = true)
    }

    /**
     * Need reset list, because user can change permission or
     * delete some files or remove sd card.
     *
     * It calls even after permission dialog.
     */
    override fun onPause() {
        interactor.resetFileList()
    }


    /**
     * Call [startExport] only if [result] equals [PermissionResult.LOW_API] or
     * [PermissionResult.GRANTED]. Otherwise we must show dialog.
     */
    override fun onClickExport(result: PermissionResult?) {
        if (result == null) return

        when (result) {
            PermissionResult.ALLOWED -> callback?.showExportPermissionDialog()
            PermissionResult.LOW_API, PermissionResult.GRANTED -> {
                viewModelScope.launch { startExport() }
            }
            PermissionResult.FORBIDDEN -> callback?.showExportDenyDialog()
        }
    }

    @RunPrivate suspend fun startExport() {
        callback?.showExportLoadingDialog()
        val result: ExportResult = runBack { interactor.export() }
        callback?.hideExportLoadingDialog()

        when (result) {
            is ExportResult.Success -> {
                callback?.showExportPathToast(result.path)

                /**
                 * Need update file list for feature import.
                 */
                callback?.updateImportEnabled(isEnabled = false)
                interactor.resetFileList()
                setupBackground()
            }
            is ExportResult.Error -> {
                callback?.showToast(R.string.pref_toast_export_error)
            }
        }
    }

    /**
     * Show permission only on [PermissionResult.ALLOWED] because we
     * can display files which not located on SD card.
     */
    override fun onClickImport(result: PermissionResult?) {
        if (result == null) return

        when (result) {
            PermissionResult.ALLOWED -> callback?.showImportPermissionDialog()
            else -> viewModelScope.launch { prepareImportDialog() }
        }
    }

    @RunPrivate suspend fun prepareImportDialog() {
        val fileList = runBack { interactor.getFileList() }
        val titleArray = fileList.map { it.name }.toTypedArray()

        if (titleArray.isEmpty()) {
            callback?.updateImportEnabled(isEnabled = false)
        } else {
            callback?.showImportDialog(titleArray)
        }
    }

    override fun onResultImport(name: String) {
        viewModelScope.launch {
            callback?.showImportLoadingDialog()
            val result: ImportResult = runBack { interactor.import(name) }
            callback?.hideImportLoadingDialog()

            when (result) {
                is ImportResult.Simple -> callback?.showToast(R.string.pref_toast_import_result)
                is ImportResult.Skip -> callback?.showImportSkipToast(result.skipCount)
                is ImportResult.Error -> callback?.showToast(R.string.pref_toast_import_error)
            }

            if (result == ImportResult.Error) return@launch

            // TODO update alarm binds (all) after adding new notes
            callback?.sendNotifyNotesBroadcast()
            callback?.sendNotifyInfoBroadcast()
        }
    }

}