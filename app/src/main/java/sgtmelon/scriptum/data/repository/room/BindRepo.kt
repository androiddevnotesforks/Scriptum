package sgtmelon.scriptum.data.repository.room

import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb

/**
 * Repository of [RoomDb] which work with notes bind in status bar.
 */
class BindRepo(override val roomProvider: RoomProvider) : IBindRepo, IRoomWork {

    // TODO test for nullable values

    override suspend fun unbindNote(id: Long): Boolean? = takeFromRoom {
        val noteEntity = noteDao.get(id)?.apply { isStatus = false }

        if (noteEntity != null) {
            noteDao.update(noteEntity)
        }

        return@takeFromRoom noteEntity != null
    }

    override suspend fun getNotificationCount(): Int? = takeFromRoom { alarmDao.getCount() }

}