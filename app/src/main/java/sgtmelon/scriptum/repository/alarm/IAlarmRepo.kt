package sgtmelon.scriptum.repository.alarm

import sgtmelon.scriptum.model.item.NotificationItem

/**
 * Interface for communication with [AlarmRepo]
 *
 * @author SerjantArbuz
 */
interface IAlarmRepo {

    fun getList(): MutableList<NotificationItem>

    fun delete(id: Long)

}