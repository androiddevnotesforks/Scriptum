package sgtmelon.scriptum.repository.room

import android.content.Context
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.model.AlarmConverter

/**
 * Repository of [RoomDb] which work with alarm
 *
 * @param context for open [RoomDb]
 */
class AlarmRepo(override val context: Context) : IAlarmRepo, IRoomWork {

    private val converter = AlarmConverter()

    override suspend fun insertOrUpdate(noteItem: NoteItem, date: String) = inRoom {
        noteItem.alarmDate = date

        val entity = converter.toEntity(noteItem)
        if (noteItem.haveAlarm()) {
            alarmDao.update(entity)
        } else {
            noteItem.alarmId = alarmDao.insert(entity)
        }
    }

    override suspend fun delete(noteId: Long) = inRoom { alarmDao.delete(noteId) }


    override suspend fun getItem(noteId: Long): NotificationItem? {
        val item: NotificationItem?

        openRoom().apply {
            item = alarmDao.getItem(noteId)
        }.close()

        return item
    }

    override suspend fun getList() = ArrayList<NotificationItem>().apply {
        inRoom { addAll(alarmDao.getList()) }
    }

}