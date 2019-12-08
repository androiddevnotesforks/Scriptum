package sgtmelon.scriptum.repository.alarm

import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem

/**
 * Interface for communicate with [AlarmRepo]
 */
interface IAlarmRepo {

    suspend fun insertOrUpdate(noteItem: NoteItem, date: String)

    suspend fun delete(noteId: Long)

    suspend fun getItem(noteId: Long): NotificationItem?

    fun getList(): MutableList<NotificationItem>

}