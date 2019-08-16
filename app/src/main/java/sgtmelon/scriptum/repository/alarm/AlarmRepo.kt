package sgtmelon.scriptum.repository.alarm

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
class AlarmRepo(override val context: Context) : IAlarmRepo, IRoomWork {

    override fun insertOrUpdate(alarmEntity: AlarmEntity) = inRoom {
        if (alarmEntity.needInsert()) {
            alarmEntity.id = iAlarmDao.insert(alarmEntity)
        } else {
            iAlarmDao.update(alarmEntity)
        }
    }

    override fun delete(id: Long) = inRoom { iAlarmDao.delete(id) }

    override fun getList() = ArrayList<NotificationItem>().apply {
        inRoom { addAll(iAlarmDao.get()) }
    }

    companion object {
        fun getInstance(context: Context): IAlarmRepo = AlarmRepo(context)
    }

}