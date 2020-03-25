package sgtmelon.scriptum.data.repository.room

import android.content.Context
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb

/**
 * Repository of [RoomDb] which work with notes bind in status bar
 *
 * @param context for open [RoomDb]
 */
class BindRepo(override val context: Context) : IBindRepo, IRoomWork {

    override suspend fun unbindNote(id: Long): Boolean {
        val result: Boolean

        openRoom().apply {
            val noteEntity = noteDao.get(id)?.apply { isStatus = false }
            noteEntity?.let { noteDao.update(it) }

            result = noteEntity != null
        }.close()

        return result
    }

    override suspend fun getNotificationCount(): Int {
        val count: Int

        openRoom().apply { count = alarmDao.getCount() }.close()

        return count
    }

}