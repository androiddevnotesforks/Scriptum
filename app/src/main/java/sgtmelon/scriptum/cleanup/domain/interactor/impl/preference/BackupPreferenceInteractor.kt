package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference

import sgtmelon.scriptum.cleanup.data.repository.room.BackupRepoImpl
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupParser
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IBackupPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.model.result.ExportResult
import sgtmelon.scriptum.cleanup.domain.model.result.ImportResult
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IBackupPreferenceViewModel
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.item.FileItem
import sgtmelon.scriptum.infrastructure.model.type.FileType

/**
 * Interactor for import/export backup files (for [IBackupPreferenceViewModel]).
 */
class BackupPreferenceInteractor(
    private val preferencesRepo: PreferencesRepo,
    private val backupRepo: BackupRepo,
    private val backupParser: IBackupParser,
    private val fileDataSource: FileDataSource,
    private val cipherDataSource: CipherDataSource
) : IBackupPreferenceInteractor {

    override suspend fun export(): ExportResult {
        val parserResult = backupRepo.getData()

        val data = backupParser.collect(parserResult)
        val encryptData = cipherDataSource.encrypt(data)

        val timeName = fileDataSource.getTimeName(FileType.BACKUP)
        val path = fileDataSource.writeFile(timeName, encryptData) ?: return ExportResult.Error

        return ExportResult.Success(path)
    }

    override suspend fun import(name: String, backupFileList: List<FileItem>): ImportResult {
        val item = backupFileList.firstOrNull { it.name == name } ?: return ImportResult.Error

        val encryptData = fileDataSource.readFile(item.path) ?: return ImportResult.Error
        val data = cipherDataSource.decrypt(encryptData)

        val parserResult = backupParser.parse(data) ?: return ImportResult.Error
        val isSkipImports = preferencesRepo.isBackupSkipImports

        return backupRepo.insertData(BackupRepoImpl.Model[parserResult], isSkipImports)
    }
}