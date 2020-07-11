package sgtmelon.scriptum.data.repository.room.callback

import sgtmelon.scriptum.data.repository.room.BackupRepo
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.result.ParserResult

/**
 * Interface for [BackupRepo].
 */
interface IBackupRepo {
    fun insertData(parserResult: ParserResult, importSkip: Boolean): ImportResult
}