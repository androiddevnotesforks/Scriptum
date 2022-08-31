package sgtmelon.scriptum.cleanup.data.repository.room.callback

import sgtmelon.scriptum.cleanup.data.repository.room.BackupRepoImpl
import sgtmelon.scriptum.cleanup.domain.model.result.ImportResult

/**
 * Interface for [BackupRepoImpl].
 */
interface IBackupRepo {
    suspend fun insertData(model: BackupRepoImpl.Model, isSkipImports: Boolean): ImportResult
}