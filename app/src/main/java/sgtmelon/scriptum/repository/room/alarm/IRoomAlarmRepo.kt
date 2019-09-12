package sgtmelon.scriptum.repository.room.alarm

import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.room.entity.AlarmEntity

/**
 * Interface for communication with [RoomAlarmRepo]
 *
 * @author SerjantArbuz
 */
interface IRoomAlarmRepo {

    fun insertOrUpdate(alarmEntity: AlarmEntity)

    fun delete(noteId: Long)

    fun getList(): MutableList<NotificationItem>

}