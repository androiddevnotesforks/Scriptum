package sgtmelon.scriptum.repository.alarm

import android.content.Context
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.AlarmEntity

/**
 * Репозиторий обработки данных [RoomDb] для работы с будильником
 *
 * @param context для открытия [RoomDb]
 *
 * @author SerjantArbuz
 */
class AlarmRepo(private val context: Context) : IAlarmRepo {

    private fun openRoom() = RoomDb.getInstance(context)

    // TODO #RELEASE получать по другому
    override fun getList() = ArrayList<NoteModel>().apply {
        openRoom().apply { addAll(getAlarmDao().getTest()) }.close()
    }

    override fun delete(alarmEntity: AlarmEntity) = openRoom().apply { getAlarmDao().delete(alarmEntity) }.close()

    companion object {
        fun getInstance(context: Context): IAlarmRepo = AlarmRepo(context)
    }

}