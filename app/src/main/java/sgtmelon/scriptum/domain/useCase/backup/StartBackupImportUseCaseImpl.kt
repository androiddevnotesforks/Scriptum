package sgtmelon.scriptum.domain.useCase.backup

import android.util.Log
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.data.backup.BackupParser
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.infrastructure.model.item.FileItem
import sgtmelon.scriptum.infrastructure.model.type.FileType

class StartBackupImportUseCaseImpl(
    private val preferencesRepo: PreferencesRepo,
    private val backupRepo: BackupRepo,
    private val backupParser: BackupParser,
    private val fileDataSource: FileDataSource,
    private val cipherDataSource: CipherDataSource
) : StartBackupImportUseCase {

    /**
     * [name] - name of [FileItem] inside [fileList]
     * [fileList] - list of backup files with extension [FileType.BACKUP]
     */
    override suspend operator fun invoke(name: String, fileList: List<FileItem>): ImportResult {
        Log.i("HERE", "start backup import")
        val item = fileList.firstOrNull { it.name == name } ?: return ImportResult.Error

        val encryptData = fileDataSource.readFile(item.path) ?: return ImportResult.Error
        val data = cipherDataSource.decrypt(encryptData)

        val parserResult = backupParser.convert(data) ?: return ImportResult.Error
        val isSkipImports = preferencesRepo.isBackupSkipImports

        return backupRepo.insertData(parserResult, isSkipImports)
    }
}