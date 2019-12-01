package sgtmelon.scriptum.repository.alarm

import android.content.Context
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.AlarmConverter

/**
 * Repository of [RoomDb] which work with alarm
 *
 * @param context for open [RoomDb]
 */
class AlarmRepo(override val context: Context) : IAlarmRepo, IRoomWork {

    private val converter = AlarmConverter()

    override suspend fun insertOrUpdate(noteItem: NoteItem, date: String) = inRoom2 {
        noteItem.alarmDate = date

        val entity = converter.toEntity(noteItem)
        if (noteItem.haveAlarm()) {
            iAlarmDao.update(entity)
        } else {
            noteItem.alarmId = iAlarmDao.insert(entity)
        }
    }

    override suspend fun delete(noteId: Long) = inRoom2 { iAlarmDao.delete(noteId) }


    override suspend fun getItem(noteItem: NoteItem): NotificationItem? {
        val item: NotificationItem?

        openRoom().apply {
            item = iAlarmDao.getItem(noteItem.id)
        }.close()

        return item
    }

    override fun getList() = ArrayList<NotificationItem>().apply {
        inRoom { addAll(iAlarmDao.getList()) }
    }

}