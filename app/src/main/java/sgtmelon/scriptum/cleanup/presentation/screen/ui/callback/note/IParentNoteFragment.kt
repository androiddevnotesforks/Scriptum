package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note

import java.util.Calendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl
import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Parent interface for [ITextNoteFragment] and [IRollNoteFragment].
 */
interface IParentNoteFragment<N : NoteItem> : SystemReceiver.Bridge.Alarm,
    SystemReceiver.Bridge.Bind {

    val isDialogOpen: Boolean

    fun hideKeyboard()


    fun onBindingEdit(item: N, isEditMode: Boolean)

    fun onBindingNote(item: N)

    fun onBindingInput(item: N, inputAccess: InputControl.Access)


    fun tintToolbar(from: Color, to: Color)

    fun tintToolbar(color: Color)


    fun changeName(text: String, cursor: Int)


    fun showRankDialog(check: Int)

    fun showColorDialog(color: Color)

    fun showDateDialog(calendar: Calendar, resetVisible: Boolean)

    fun showTimeDialog(calendar: Calendar, dateList: List<String>)

    fun showConvertDialog()


    fun showSaveToast(isSuccess: Boolean)

}