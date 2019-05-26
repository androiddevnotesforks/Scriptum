package sgtmelon.scriptum.model.item

import sgtmelon.scriptum.adapter.NotificationAdapter
import sgtmelon.scriptum.model.NoteModel

/**
 * Элемент списка для [NotificationAdapter] и информации о заметке [NoteModel]
 */
class NotificationItem(var id: Long = 0, var noteId: Long = 0, var date: String = "")