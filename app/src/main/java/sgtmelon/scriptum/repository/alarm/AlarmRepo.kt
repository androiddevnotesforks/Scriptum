package sgtmelon.scriptum.repository.alarm

import android.content.Context
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.RoomDb

/**
 * Репозиторий обработки данных [RoomDb] для работы с будильником
 *
 * @param context для открытия [RoomDb]
 *
 * @author SerjantArbuz
 */
class AlarmRepo(override val context: Context) : IAlarmRepo, IRoomWork {

    override fun getList() = ArrayList<NotificationItem>().apply {
        inTheRoom { addAll(getAlarmDao().get()) }
    }

    override fun delete(id: Long) = inTheRoom { getAlarmDao().delete(id) }

    companion object {
        fun getInstance(context: Context): IAlarmRepo = AlarmRepo(context)
    }

}