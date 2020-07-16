package sgtmelon.scriptum.data.repository.room.callback

import sgtmelon.scriptum.data.repository.room.BackupRepo
import sgtmelon.scriptum.domain.model.result.ImportResult

/**
 * Interface for [BackupRepo].
 */
interface IBackupRepo {
    suspend fun insertData(model: BackupRepo.Model, importSkip: Boolean): ImportResult
}