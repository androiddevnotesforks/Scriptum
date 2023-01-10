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

    fun hideKeyboard()


    fun onBindingEdit(item: N, isEditMode: Boolean)

    fun onBindingNote(item: N)


    fun onPressBack(): Boolean

    fun tintToolbar(from: Color, to: Color)

    fun tintToolbar(color: Color)

    fun setToolbarBackIcon(isCancel: Boolean, needAnim: Boolean)


    fun changeName(text: String, cursor: Int)

    fun showSaveToast(isSuccess: Boolean)

    fun finish()

}