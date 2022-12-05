package sgtmelon.scriptum.cleanup.data.repository.room.callback

import sgtmelon.scriptum.cleanup.data.repository.room.BackupRepoImpl
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.result.ParserResult

/**
 * Interface for [BackupRepoImpl].
 */
interface BackupRepo {

    suspend fun getData(): ParserResult.Export

    suspend fun insertData(result: ParserResult.Import, isSkipImports: Boolean): ImportResult
}