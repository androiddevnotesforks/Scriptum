package sgtmelon.scriptum.repository.alarm

import android.content.Context
import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.AlarmConverter
import sgtmelon.scriptum.room.entity.AlarmEntity

/**
 * Repository of [RoomDb] which work with alarm
 *
 * @param context for open [RoomDb]
 */
class AlarmRepo(override val context: Context) : IAlarmRepo, IRoomWork {

    private val converter = AlarmConverter()

    override fun insertOrUpdate(alarmEntity: AlarmEntity) = inRoom {
        if (alarmEntity.needInsert()) {
            alarmEntity.id = iAlarmDao.insert(alarmEntity)
        } else {
            iAlarmDao.update(alarmEntity)
        }
    }

    override fun insertOrUpdate(noteItem: NoteItem, date: String) = inRoom {
        noteItem.alarmDate = date

        val entity = converter.toEntity(noteItem)
        if (noteItem.haveAlarm()) {
            iAlarmDao.update(entity)
        } else {
            noteItem.alarmId = iAlarmDao.insert(entity)
        }
    }

    override fun delete(noteId: Long) = inRoom { iAlarmDao.delete(noteId) }

    override fun update(noteItem: NoteItem) = inRoom {
        val alarm = iAlarmDao[noteItem.id] ?: return@inRoom

        noteItem.apply {
            alarmId = alarm.id
            alarmDate = alarm.date
        }
    }

    override fun getList() = ArrayList<NotificationItem>().apply {
        inRoom { addAll(iAlarmDao.get()) }
    }

}