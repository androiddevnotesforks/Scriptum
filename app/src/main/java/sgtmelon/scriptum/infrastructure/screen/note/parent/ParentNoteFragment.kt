package sgtmelon.scriptum.infrastructure.screen.note.parent

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.receiver.service.EternalServiceReceiver

/**
 * Parent interface for [TextNoteFragment] and [RollNoteFragment].
 */
@Deprecated("Don't use it")
interface ParentNoteFragment<N : NoteItem> : EternalServiceReceiver.Bridge.Bind {

    val isDialogOpen: Boolean

    /**
     * FALSE result will call super.onBackPress() in parent activity.
     */
    fun onPressBack(): Boolean


    fun changeName(text: String, cursor: Int)

    fun finish()

}