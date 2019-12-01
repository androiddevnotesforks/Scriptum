package sgtmelon.scriptum.repository.alarm

import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.room.entity.AlarmEntity

/**
 * Interface for communicate with [AlarmRepo]
 */
interface IAlarmRepo {

    suspend fun insertOrUpdate(noteItem: NoteItem, date: String)

    suspend fun delete(noteId: Long)

    suspend fun getItem(noteItem: NoteItem): NotificationItem?

    fun getList(): MutableList<NotificationItem>

}