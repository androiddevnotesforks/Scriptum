package sgtmelon.scriptum.infrastructure.screen.note.parent

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.receiver.service.EternalServiceReceiver

/**
 * Parent interface for [TextNoteFragment] and [RollNoteFragment].
 */
@Deprecated("Don't use it")
interface ParentNoteFragment<N : NoteItem> : EternalServiceReceiver.Bridge.Bind {

    val isDialogOpen: Boolean

    fun onBindingEdit(item: N, isEditMode: Boolean)


    fun onPressBack(): Boolean

    fun tintToolbar(from: Color, to: Color)


    fun changeName(text: String, cursor: Int)

    fun showSaveToast(isSuccess: Boolean)

    fun finish()

}