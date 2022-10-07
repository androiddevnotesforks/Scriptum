package sgtmelon.scriptum.data.repository.database

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

interface AlarmRepo {

    /**
     * Returns new alarm id (if done work without conflicts).
     */
    suspend fun insertOrUpdate(item: NoteItem, date: String): Long?

    suspend fun delete(noteId: Long)

    suspend fun getList(): List<NotificationItem>

    suspend fun getDateList(): List<String>
}