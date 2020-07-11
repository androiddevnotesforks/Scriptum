package sgtmelon.scriptum.data.repository.room

import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.callback.IBackupRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.result.ParserResult

/**
 * Repository of [RoomDb] which work with backup data.
 */
class BackupRepo(override val roomProvider: RoomProvider) : IBackupRepo, IRoomWork {

    override fun insertData(parserResult: ParserResult, importSkip: Boolean): ImportResult {
        TODO("Not yet implemented")
    }
}