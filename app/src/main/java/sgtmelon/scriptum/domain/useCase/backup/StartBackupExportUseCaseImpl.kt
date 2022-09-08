package sgtmelon.scriptum.domain.useCase.backup

import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.data.backup.BackupCollector
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.domain.model.result.ExportResult

class StartBackupExportUseCaseImpl(
    private val backupRepo: BackupRepo,
    private val backupCollector: BackupCollector,
    private val fileDataSource: FileDataSource,
    private val cipherDataSource: CipherDataSource
) : StartBackupExportUseCase {

    override suspend operator fun invoke(): ExportResult {
        val parserResult = backupRepo.getData()

        val data = backupCollector.convert(parserResult) ?: return ExportResult.Error
        val encryptData = cipherDataSource.encrypt(data)

        val timeName = fileDataSource.getBackupName()
        val path = fileDataSource.writeFile(timeName, encryptData) ?: return ExportResult.Error

        return ExportResult.Success(path)
    }
}