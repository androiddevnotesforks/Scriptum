package sgtmelon.scriptum.repository.alarm

import android.content.Context
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.AlarmEntity

/**
 * Repository of [RoomDb] which work with alarm
 *
 * @param context for open [RoomDb]
 */
class AlarmRepo(override val context: Context) : IAlarmRepo, IRoomWork {

    override fun insertOrUpdate(alarmEntity: AlarmEntity) = inRoom {
        if (alarmEntity.needInsert()) {
            alarmEntity.id = iAlarmDao.insert(alarmEntity)
        } else {
            iAlarmDao.update(alarmEntity)
        }
    }

    override fun delete(noteId: Long) = inRoom { iAlarmDao.delete(noteId) }

    override fun get(noteId: Long): AlarmEntity {
        val item: AlarmEntity

        openRoom().apply {
            item = iAlarmDao[noteId] ?: AlarmEntity(noteId = noteId)
        }.close()

        return item
    }

    override fun getList() = ArrayList<NotificationItem>().apply {
        inRoom { addAll(iAlarmDao.get()) }
    }

}