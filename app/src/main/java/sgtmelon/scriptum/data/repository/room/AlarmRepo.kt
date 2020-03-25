package sgtmelon.scriptum.data.repository.room

import android.content.Context
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem

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