package sgtmelon.scriptum.repository.alarm

import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.room.entity.AlarmEntity

/**
 * Interface for communicate with [AlarmRepo]
 */
interface IAlarmRepo {

    fun insertOrUpdate(alarmEntity: AlarmEntity)

    fun delete(noteId: Long)

    fun getList(): MutableList<NotificationItem>

}