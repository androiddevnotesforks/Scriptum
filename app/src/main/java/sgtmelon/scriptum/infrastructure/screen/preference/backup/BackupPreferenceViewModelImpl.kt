package sgtmelon.scriptum.infrastructure.screen.preference.backup

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import sgtmelon.extensions.onBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.useCase.backup.GetBackupFileListUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupExportUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupImportUseCase
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ExportState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportDataState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportState
import sgtmelon.test.prod.RunPrivate
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult as Permission

class BackupPreferenceViewModelImpl(
    private val getBackupFileList: GetBackupFileListUseCase,
    private val startBackupExport: StartBackupExportUseCase,
    private val startBackupImport: StartBackupImportUseCase
) : ViewModel(),
    BackupPreferenceViewModel {

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

    //region Export/import functions

    //    override fun prepareImport() {
    //        viewModelScope.launch { prepareImportDialog() }
    //    }

    //    @RunPrivate suspend fun prepareImportDialog() {
    //        val fileList = runBack { getBackupFileList() }
    //        val titleArray = fileList.map { it.name }.toTypedArray()
    //
    //        if (titleArray.isEmpty()) {
    //            callback?.updateImportSummary(R.string.pref_summary_backup_import_empty)
    //            callback?.updateImportEnabled(isEnabled = false)
    //        } else {
    //            callback?.showImportDialog(titleArray)
    //        }
    //    }

    //    override fun onResultImport(name: String) {
    //        viewModelScope.launch {
    //            callback?.showImportLoadingDialog()
    //            val result = runBack { startBackupImport(name, getBackupFileList()) }
    //            callback?.hideImportLoadingDialog()
    //
    //            when (result) {
    //                is ImportResult.Simple -> callback?.showToast(R.string.pref_toast_import_result)
    //                is ImportResult.Skip -> callback?.showImportSkipToast(result.skipCount)
    //                is ImportResult.Error -> callback?.showToast(R.string.pref_toast_import_error)
    //            }
    //
    //            if (result == ImportResult.Error) return@launch
    //
    //            callback?.sendTidyUpAlarmBroadcast()
    //            callback?.sendNotifyNotesBroadcast()
    //            callback?.sendNotifyInfoBroadcast()
    //        }
    //    }

    //endregion

    override fun startExport(): Flow<ExportState> = flow {
        emit(ExportState.ShowLoading)
        val result = startBackupExport()
        emit(ExportState.HideLoading)

        when (result) {
            is ExportResult.Success -> {
                emit(ExportState.LoadSuccess(result.path))

                /** Need update file list for feature import. */
                getBackupFileList.reset()
                setupBackground()
            }
            is ExportResult.Error -> emit(ExportState.LoadError)
        }
    }.onBack()

    override val importData: Flow<ImportDataState>
        get() = flow {
            val titleArray = getBackupFileList().map { it.name }.toTypedArray()
            if (titleArray.isEmpty()) {
                emit(ImportDataState.Empty)
            } else {
                emit(ImportDataState.Normal(titleArray))
            }
        }.onBack()

    override fun startImport(name: String): Flow<ImportState> = flow {
        emit(ImportState.ShowLoading)
        val result = startBackupImport(name, getBackupFileList())
        emit(ImportState.HideLoading)

        emit(
            when (result) {
                is ImportResult.Simple -> ImportState.LoadSuccess
                is ImportResult.Skip -> ImportState.LoadSkip(result.skipCount)
                is ImportResult.Error -> ImportState.LoadError
            }
        )

        if (result == ImportResult.Error) return@flow

        emit(ImportState.Finish)
    }.onBack()
}