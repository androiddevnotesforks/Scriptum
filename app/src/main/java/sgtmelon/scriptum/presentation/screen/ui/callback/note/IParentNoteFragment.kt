package sgtmelon.scriptum.presentation.screen.ui.callback.note

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import java.util.*

/**
 * Parent interface for [ITextNoteFragment] and [IRollNoteFragment].
 */
interface IParentNoteFragment<N : NoteItem> : IParentNoteBridge {

    val isDialogOpen: Boolean

    fun hideKeyboard()


    fun onBindingEdit(item: N, isEditMode: Boolean)

    fun onBindingNote(item: N)

    fun onBindingInput(item: N, inputAccess: InputControl.Access)


    fun tintToolbar(@Color from: Int, @Color to: Int)

    fun tintToolbar(@Color color: Int)


    fun changeName(text: String, cursor: Int)


    fun showRankDialog(check: Int)

    fun showColorDialog(@Color color: Int, @Theme theme: Int)

    fun showDateDialog(calendar: Calendar, resetVisible: Boolean)

    fun showTimeDialog(calendar: Calendar, dateList: List<String>)

    fun showConvertDialog()


    fun showSaveToast(success: Boolean)

}