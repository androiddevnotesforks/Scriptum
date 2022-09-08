package sgtmelon.scriptum.cleanup.data.repository.room.callback

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

interface AlarmRepo {

    suspend fun insertOrUpdate(item: NoteItem, date: String)

    suspend fun delete(noteId: Long)

    suspend fun getItem(noteId: Long): NotificationItem?

    suspend fun getList(): List<NotificationItem>

    suspend fun getDateList(): List<String>
}