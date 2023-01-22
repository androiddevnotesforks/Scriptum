package sgtmelon.scriptum.infrastructure.screen.note.parent

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.receiver.service.EternalServiceReceiver

/**
 * Parent interface for [TextNoteFragment] and [RollNoteFragment].
 */
@Deprecated("Don't use it")
interface ParentNoteFragment<N : NoteItem> : EternalServiceReceiver.Bridge.Bind {

    fun changeName(text: String, cursor: Int)

}