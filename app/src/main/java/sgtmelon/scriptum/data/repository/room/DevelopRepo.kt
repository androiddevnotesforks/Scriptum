package sgtmelon.scriptum.data.repository.room

import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb

/**
 * Repository of [RoomDb] which work with all tables data and only for developer screen.
 */
class DevelopRepo(
    override val roomProvider: RoomProvider
) : IDevelopRepo,
    IRoomWork {

    // tODO add tests


    override suspend fun getRandomNoteId(): Long = takeFromRoom {
        noteDao.get(bin = false).random().id
    }
}