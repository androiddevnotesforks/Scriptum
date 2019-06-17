package sgtmelon.scriptum.repository.alarm

import sgtmelon.scriptum.model.item.NotificationItem

/**
 * Интерфейс для общения с [AlarmRepo]
 *
 * @author SerjantArbuz
 */
interface IAlarmRepo {

    fun getList(): MutableList<NotificationItem>

    fun delete(id: Long)

}