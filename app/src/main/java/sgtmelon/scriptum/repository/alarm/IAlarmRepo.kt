package sgtmelon.scriptum.repository.alarm

import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem

/**
 * Interface for communicate with [AlarmRepo]
 */
interface IAlarmRepo {

    fun insertOrUpdate(noteItem: NoteItem, date: String)

    fun delete(noteId: Long)

    suspend fun getItem(noteItem: NoteItem): NotificationItem?

    fun getList(): MutableList<NotificationItem>

}