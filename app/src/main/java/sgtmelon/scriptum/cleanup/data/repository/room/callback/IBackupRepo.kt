package sgtmelon.scriptum.cleanup.data.repository.room.callback

import sgtmelon.scriptum.cleanup.data.repository.room.BackupRepo
import sgtmelon.scriptum.cleanup.domain.model.result.ImportResult

/**
 * Interface for [BackupRepo].
 */
interface IBackupRepo {
    suspend fun insertData(model: BackupRepo.Model, importSkip: Boolean): ImportResult
}