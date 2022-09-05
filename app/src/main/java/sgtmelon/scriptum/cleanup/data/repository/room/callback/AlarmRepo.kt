package sgtmelon.scriptum.cleanup.data.repository.room.callback

import java.util.Calendar
import sgtmelon.scriptum.cleanup.data.repository.room.AlarmRepoImpl
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

/**
 * Interface for communicate with [AlarmRepoImpl]
 */
@Deprecated("Use useCases")
interface AlarmRepo {

    suspend fun insertOrUpdate(item: NoteItem, calendar: Calendar)

    suspend fun insertOrUpdate(item: NoteItem, date: String)

    suspend fun delete(noteId: Long)

    suspend fun getItem(noteId: Long): NotificationItem?

    suspend fun getList(): List<NotificationItem>
}