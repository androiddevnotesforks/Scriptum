package sgtmelon.scriptum.data.repository.room.callback

import sgtmelon.scriptum.data.repository.room.AlarmRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem

/**
 * Interface for communicate with [AlarmRepo]
 */
interface IAlarmRepo {

    suspend fun insertOrUpdate(noteItem: NoteItem, date: String)

    suspend fun delete(noteId: Long)

    suspend fun getItem(noteId: Long): NotificationItem?

    suspend fun getList(): MutableList<NotificationItem>

}