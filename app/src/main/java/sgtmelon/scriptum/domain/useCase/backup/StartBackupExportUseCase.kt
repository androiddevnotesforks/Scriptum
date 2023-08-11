package sgtmelon.scriptum.domain.useCase.backup

import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.data.backup.BackupCollector
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ExportResult.Error
import sgtmelon.scriptum.infrastructure.model.key.FileType
import sgtmelon.scriptum.infrastructure.model.key.AppError.File as FileError

class StartBackupExportUseCase(
    private val backupRepo: BackupRepo,
    private val backupCollector: BackupCollector,
    private val fileDataSource: FileDataSource,
    private val cipherDataSource: CipherDataSource
) {

    suspend operator fun invoke(): ExportResult {
        val parserResult = backupRepo.getData()

        val data = backupCollector.convert(parserResult) ?: return Error(FileError.Data)

        val encryptData = cipherDataSource.encrypt(data)

        val timeName = fileDataSource.getTimeName(FileType.BACKUP)
        val path = fileDataSource.writeFile(timeName, encryptData) ?: return Error(FileError.Create)

        return ExportResult.Success(path)
    }
}