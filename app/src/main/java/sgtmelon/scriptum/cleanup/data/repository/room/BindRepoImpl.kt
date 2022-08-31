package sgtmelon.scriptum.cleanup.data.repository.room

import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.cleanup.data.room.IRoomWork
import sgtmelon.scriptum.cleanup.data.room.extension.fromRoom
import sgtmelon.scriptum.cleanup.data.room.extension.inRoom
import sgtmelon.scriptum.infrastructure.database.Database

/**
 * Repository of [Database] which work with notes bind in status bar.
 */
class BindRepoImpl(override val roomProvider: RoomProvider) : BindRepo, IRoomWork {

    override suspend fun unbindNote(id: Long) = inRoom {
        val noteEntity = noteDao.get(id) ?: return@inRoom

        if (!noteEntity.isStatus) return@inRoom

        noteEntity.isStatus = false
        noteDao.update(noteEntity)
    }

    override suspend fun getNotificationCount(): Int = fromRoom { alarmDao.getCount() }

}