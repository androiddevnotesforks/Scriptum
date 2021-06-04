package sgtmelon.scriptum.data.repository.room

import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.extension.fromRoom
import sgtmelon.scriptum.data.room.extension.inRoom

/**
 * Repository of [RoomDb] which work with notes bind in status bar.
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