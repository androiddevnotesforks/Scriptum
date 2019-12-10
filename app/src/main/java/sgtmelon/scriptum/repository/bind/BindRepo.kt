package sgtmelon.scriptum.repository.bind

import android.content.Context
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.NoteEntity

/**
 * Repository of [RoomDb] which work with notes bind in status bar
 *
 * @param context for open [RoomDb]
 */
class BindRepo(override val context: Context) : IBindRepo, IRoomWork {

    override suspend fun unbindNote(id: Long): Boolean {
        val result: Boolean

        openRoom().apply {
            val noteEntity = iNoteDao.get(id)?.apply { isStatus = false }
            noteEntity?.let { iNoteDao.update(it) }

            result = noteEntity != null
        }.close()

        return result
    }

    override suspend fun getNotificationCount(): Int {
        val count: Int

        openRoom().apply { count = iAlarmDao.getCount() }.close()

        return count
    }

}