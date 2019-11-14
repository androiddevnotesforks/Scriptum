package sgtmelon.scriptum.repository.alarm

import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.room.entity.AlarmEntity

/**
 * Interface for communicate with [AlarmRepo]
 */
interface IAlarmRepo {

    fun insertOrUpdate(alarmEntity: AlarmEntity)

    fun insertOrUpdate(noteItem: NoteItem, date: String)

    fun delete(noteId: Long)

    fun update(noteItem: NoteItem)

    fun getList(): MutableList<NotificationItem>

}