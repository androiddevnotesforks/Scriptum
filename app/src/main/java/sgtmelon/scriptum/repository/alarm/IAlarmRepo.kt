package sgtmelon.scriptum.repository.alarm

import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.room.entity.AlarmEntity

/**
 * Interface for communication with [AlarmRepo]
 *
 * @author SerjantArbuz
 */
interface IAlarmRepo {

    fun insertOrUpdate(alarmEntity: AlarmEntity)

    fun delete(id: Long)

    fun getList(): MutableList<NotificationItem>

}