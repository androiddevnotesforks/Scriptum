package sgtmelon.scriptum.domain.useCase.backup

import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.data.backup.BackupParser
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.result.ImportResult.Error
import sgtmelon.scriptum.infrastructure.model.item.FileItem
import sgtmelon.scriptum.infrastructure.model.key.FileType
import sgtmelon.scriptum.infrastructure.model.key.AppError.File as FileError

class StartBackupImportUseCase(
    private val preferencesRepo: PreferencesRepo,
    private val backupRepo: BackupRepo,
    private val backupParser: BackupParser,
    private val fileDataSource: FileDataSource,
    private val cipherDataSource: CipherDataSource
) {

    /**
     * [name] - name of [FileItem] inside [fileList]
     * [fileList] - list of backup files with extension [FileType.BACKUP]
     */
    suspend operator fun invoke(name: String, fileList: List<FileItem>): ImportResult {
        val item = fileList.firstOrNull { it.name == name } ?: return Error(FileError.NotFound)
        val encryptData = fileDataSource.readFileFromPath(item.path) ?: return Error(FileError.Read)
        return startImport(encryptData)
    }

    suspend operator fun invoke(uri: String): ImportResult {
        val encryptData = fileDataSource.readFileFromUri(uri) ?: return Error(FileError.Read)
        return startImport(encryptData)
    }

    private suspend fun startImport(encryptData: String): ImportResult {
        val data = cipherDataSource.decrypt(encryptData) ?: return Error(FileError.Decode)
        val parserResult = backupParser.convert(data) ?: return Error(FileError.Damaged)
        val isSkipImports = preferencesRepo.isBackupSkip

        return backupRepo.insertData(parserResult, isSkipImports)
    }
}