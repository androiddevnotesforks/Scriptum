package sgtmelon.scriptum.domain.useCase.backup

import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParser
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.infrastructure.model.type.FileType

class StartBackupExportUseCaseImpl(
    private val backupRepo: BackupRepo,
    private val backupParser: BackupParser,
    private val fileDataSource: FileDataSource,
    private val cipherDataSource: CipherDataSource
) : StartBackupExportUseCase {

    override suspend operator fun invoke(): ExportResult {
        val parserResult = backupRepo.getData()

        val data = backupParser.collect(parserResult)
        val encryptData = cipherDataSource.encrypt(data)

        val timeName = fileDataSource.getTimeName(FileType.BACKUP)
        val path = fileDataSource.writeFile(timeName, encryptData) ?: return ExportResult.Error

        return ExportResult.Success(path)
    }
}