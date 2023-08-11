package sgtmelon.scriptum.infrastructure.screen.preference.backup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowBack
import sgtmelon.extensions.launchBack
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.useCase.backup.GetBackupFileListUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupExportUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupImportUseCase
import sgtmelon.scriptum.domain.useCase.files.GetSavePathUseCase
import sgtmelon.scriptum.infrastructure.model.item.BackupImportItem
import sgtmelon.scriptum.infrastructure.model.key.permission.PermissionResult
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ExportState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ExportSummaryState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportSummaryState

/**
 * [isFilesAutoFetch] - TRUE -> files will be fetched automatically, FALSE -> user need to
 * select files manually, with file picker.
 */
class BackupPreferenceViewModelImpl(
    private val isFilesAutoFetch: Boolean,
    private val getSavePath: GetSavePathUseCase,
    private val getBackupFileList: GetBackupFileListUseCase,
    private val startBackupExport: StartBackupExportUseCase,
    private val startBackupImport: StartBackupImportUseCase
) : ViewModel(),
    BackupPreferenceViewModel {

    private var autoFetchJob: Job? = null

    override val exportSummary: MutableLiveData<ExportSummaryState> = MutableLiveData()

    override val exportEnabled: MutableLiveData<Boolean> = MutableLiveData()

    override val importSummary: MutableLiveData<ImportSummaryState> = MutableLiveData()

    override val importEnabled: MutableLiveData<Boolean> = MutableLiveData()

    override fun updateData(permission: PermissionResult?) {
        when (permission ?: return) {
            PermissionResult.ASK -> blockBackup()
            PermissionResult.FORBIDDEN -> blockBackup()
            PermissionResult.GRANTED -> changeBackupState()
            PermissionResult.NEW_API -> changeBackupState()
        }
    }

    private fun blockBackup() {
        exportEnabled.postValue(true)
        exportSummary.postValue(ExportSummaryState.Permission)
        importEnabled.postValue(true)
        importSummary.postValue(ImportSummaryState.Permission)
    }

    private fun changeBackupState() {
        if (isFilesAutoFetch) {
            autoFetchBackup()
        } else {
            manualBackup()
        }
    }

    /** Call it only if [isFilesAutoFetch] == true. */
    private fun autoFetchBackup() {
        getBackupFileList.reset()

        autoFetchJob?.cancel()
        autoFetchJob = viewModelScope.launchBack {
            startAutoFetchBackup()
            autoFetchJob = null
        }
    }

    private suspend fun startAutoFetchBackup() {
        exportEnabled.postValue(false)
        exportSummary.postValue(ExportSummaryState.Path(getSavePath()))
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

    private fun manualBackup() {
        exportEnabled.postValue(true)
        exportSummary.postValue(ExportSummaryState.Path(getSavePath()))
        importEnabled.postValue(true)
        importSummary.postValue(ImportSummaryState.Manual)
    }

    override fun startExport(): Flow<ExportState> = flowBack {
        emit(ExportState.ShowLoading)

        when (val it = startBackupExport()) {
            is ExportResult.Success -> {
                emit(ExportState.LoadSuccess)
                emit(ExportState.Finish)

                /** Need update file list for future use of import feature. */
                if (isFilesAutoFetch) {
                    autoFetchBackup()
                }
            }
            is ExportResult.Error -> {
                emit(ExportState.LoadError(it.value))
                emit(ExportState.Finish)
            }
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

    override fun startImport(item: BackupImportItem): Flow<ImportState> = flowBack {
        emit(ImportState.ShowLoading)

        val result = when (item) {
            is BackupImportItem.AutoFetch -> startBackupImport(item.name, getBackupFileList())
            is BackupImportItem.Manual -> startBackupImport(item.uri)
        }

        when (result) {
            is ImportResult.Simple -> emit(ImportState.LoadSuccess)
            is ImportResult.Skip -> emit(ImportState.LoadSkip(result.skipCount))
            is ImportResult.Error -> emit(ImportState.LoadError(result.value))
        }

        emit(ImportState.Finish)
    }
}