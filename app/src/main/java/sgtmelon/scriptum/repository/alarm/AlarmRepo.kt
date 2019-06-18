package sgtmelon.scriptum.repository.alarm

import android.content.Context
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.room.RoomDb

/**
 * Репозиторий обработки данных [RoomDb] для работы с будильником
 *
 * @param context для открытия [RoomDb]
 *
 * @author SerjantArbuz
 */
class AlarmRepo(private val context: Context) : IAlarmRepo {

    private fun openRoom() = RoomDb.getInstance(context)

    override fun getList() = ArrayList<NotificationItem>().apply {
        openRoom().apply { addAll(getAlarmDao().get()) }.close()
    }

    override fun delete(id: Long) = openRoom().apply { getAlarmDao().delete(id) }.close()

    companion object {
        fun getInstance(context: Context): IAlarmRepo = AlarmRepo(context)
    }

}