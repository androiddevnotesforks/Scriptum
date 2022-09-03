package sgtmelon.scriptum.domain.useCase.backup

import sgtmelon.scriptum.cleanup.data.repository.room.BackupRepoImpl
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupParser
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.infrastructure.model.item.FileItem
import sgtmelon.scriptum.infrastructure.model.type.FileType

class StartBackupImportUseCaseImpl(
    private val preferencesRepo: PreferencesRepo,
    private val backupRepo: BackupRepo,
    private val backupParser: IBackupParser,
    private val fileDataSource: FileDataSource,
    private val cipherDataSource: CipherDataSource
) : StartBackupImportUseCase {

    /**
     * [name] - name of [FileItem] inside [fileList]
     * [fileList] - list of backup files with extension [FileType.BACKUP]
     */
    override suspend operator fun invoke(name: String, fileList: List<FileItem>): ImportResult {
        val item = fileList.firstOrNull { it.name == name } ?: return ImportResult.Error

        val encryptData = fileDataSource.readFile(item.path) ?: return ImportResult.Error
        val data = cipherDataSource.decrypt(encryptData)

        val parserResult = backupParser.parse(data) ?: return ImportResult.Error
        val isSkipImports = preferencesRepo.isBackupSkipImports

        return backupRepo.insertData(BackupRepoImpl.Model[parserResult], isSkipImports)
    }
}