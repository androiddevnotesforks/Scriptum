package sgtmelon.scriptum.cleanup.data.repository.room

import sgtmelon.scriptum.cleanup.data.provider.RoomProvider
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.cleanup.data.room.IRoomWork
import sgtmelon.scriptum.cleanup.data.room.RoomDb
import sgtmelon.scriptum.cleanup.data.room.converter.model.AlarmConverter
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.extension.fromRoom
import sgtmelon.scriptum.cleanup.data.room.extension.inRoom
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

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


    override suspend fun getItem(noteId: Long): NotificationItem? = fromRoom {
        alarmDao.getItem(noteId)
    }

    override suspend fun getList(): MutableList<NotificationItem> = fromRoom {
        alarmDao.getList()
    }


    override suspend fun getAlarmBackup(noteIdList: List<Long>): List<AlarmEntity> = fromRoom {
        alarmDao.get(noteIdList)
    }

}