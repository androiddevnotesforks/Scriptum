package sgtmelon.scriptum.repository.room.alarm

import android.content.Context
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.AlarmEntity

/**
 * Репозиторий обработки данных [RoomDb] для работы с будильником
 *
 * @param context для открытия [RoomDb]
 *
 * @author SerjantArbuz
 */
class RoomAlarmRepo(override val context: Context) : IRoomAlarmRepo, IRoomWork {

    override fun insertOrUpdate(alarmEntity: AlarmEntity) = inRoom {
        if (alarmEntity.needInsert()) {
            alarmEntity.id = iAlarmDao.insert(alarmEntity)
        } else {
            iAlarmDao.update(alarmEntity)
        }
    }

    override fun delete(noteId: Long) = inRoom { iAlarmDao.delete(noteId) }

    override fun getList() = ArrayList<NotificationItem>().apply {
        inRoom { addAll(iAlarmDao.get()) }
    }

}