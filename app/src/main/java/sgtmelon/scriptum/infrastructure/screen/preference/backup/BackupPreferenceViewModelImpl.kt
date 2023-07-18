package sgtmelon.scriptum.infrastructure.screen.preference.backup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowBack
import sgtmelon.extensions.launchBack
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.useCase.backup.GetBackupFileListUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupExportUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupImportUseCase
import sgtmelon.scriptum.infrastructure.model.key.permission.PermissionResult
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ExportState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ExportSummaryState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportSummaryState

class BackupPreferenceViewModelImpl(
    private val getBackupFileList: GetBackupFileListUseCase,
    private val startBackupExport: StartBackupExportUseCase,
    private val startBackupImport: StartBackupImportUseCase
) : ViewModel(),
    BackupPreferenceViewModel {

    override val exportSummary: MutableLiveData<ExportSummaryState> = MutableLiveData()

    override val exportEnabled: MutableLiveData<Boolean> = MutableLiveData()

    override val importSummary: MutableLiveData<ImportSummaryState> = MutableLiveData()

    override val importEnabled: MutableLiveData<Boolean> = MutableLiveData()

    override fun updateData(permission: PermissionResult?) {
        if (permission == PermissionResult.GRANTED) {
            viewModelScope.launchBack { updateBackupFiles() }
        } else {
            exportEnabled.postValue(true)
            exportSummary.postValue(ExportSummaryState.Permission)
            importEnabled.postValue(true)
            importSummary.postValue(ImportSummaryState.Permission)
        }
    }

    private suspend fun updateBackupFiles() {
        exportEnabled.postValue(false)
        exportSummary.postValue(ExportSummaryState.Empty)
        importEnabled.postValue(false)
        importSummary.postValue(ImportSummaryState.StartSearch)

        val fileList = getBackupFileList()

        if (fileList.isEmpty()) {
            importSummary.postValue(ImportSummaryState.NoFound)
        } else {
            importSummary.postValue(ImportSummaryState.Found(fileList.size))
            importEnabled.postValue(true)
        }

        exportEnabled.postValue(true)
    }

    override fun startExport(): Flow<ExportState> = flowBack {
        emit(ExportState.ShowLoading)
        val result = startBackupExport()
        emit(ExportState.HideLoading)

        when (result) {
            is ExportResult.Success -> {
                emit(ExportState.LoadSuccess(result.path))

                /** Need update file list for future use of import feature. */
                getBackupFileList.reset()
                updateBackupFiles()
            }
            is ExportResult.Error -> emit(ExportState.LoadError)
        }
    }

    override val importData: Flow<Array<String>>
        get() = flowBack {
            val titleArray = getBackupFileList().map { it.name }.toTypedArray()

            if (titleArray.isEmpty()) {
                importSummary.postValue(ImportSummaryState.NoFound)
                importEnabled.postValue(false)
            } else {
                emit(titleArray)
            }
        }

    override fun startImport(name: String): Flow<ImportState> = flowBack {
        emit(ImportState.ShowLoading)
        val result = startBackupImport(name, getBackupFileList())
        emit(ImportState.HideLoading)

        when (result) {
            is ImportResult.Simple -> emit(ImportState.LoadSuccess)
            is ImportResult.Skip -> emit(ImportState.LoadSkip(result.skipCount))
            is ImportResult.Error -> emit(ImportState.LoadError)
        }

        if (result != ImportResult.Error) {
            emit(ImportState.Finish)
        }
    }
}