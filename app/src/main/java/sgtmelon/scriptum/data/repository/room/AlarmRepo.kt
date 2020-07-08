package sgtmelon.scriptum.data.repository.room

import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.room.IRoomWork
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem

/**
 * Repository of [RoomDb] which work with alarm.
 */
class AlarmRepo(
        override val roomProvider: RoomProvider,
        private val converter: AlarmConverter
) : IAlarmRepo,
        IRoomWork {

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


    override suspend fun getItem(noteId: Long): NotificationItem? = takeFromRoom {
        alarmDao.getItem(noteId)
    }

    override suspend fun getList(): MutableList<NotificationItem> = takeFromRoom {
        alarmDao.getList()
    }


    override suspend fun getAlarmBackup(noteIdList: List<Long>): List<AlarmEntity> {
        return takeFromRoom { alarmDao.get(noteIdList) }
    }

}