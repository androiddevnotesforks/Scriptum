package sgtmelon.scriptum.cleanup.data.repository.room

import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.cleanup.data.room.Database
import sgtmelon.scriptum.cleanup.data.room.IRoomWork
import sgtmelon.scriptum.cleanup.data.room.extension.fromRoom
import sgtmelon.scriptum.cleanup.data.room.extension.inRoom

/**
 * Repository of [Database] which work with notes bind in status bar.
 */
class BindRepo(override val roomProvider: RoomProvider) : IBindRepo, IRoomWork {

    override suspend fun unbindNote(id: Long) = inRoom {
        val noteEntity = noteDao.get(id) ?: return@inRoom

        if (!noteEntity.isStatus) return@inRoom

        noteEntity.isStatus = false
        noteDao.update(noteEntity)
    }

    override suspend fun getNotificationCount(): Int = fromRoom { alarmDao.getCount() }

}