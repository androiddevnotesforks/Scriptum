package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note

import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl
import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver
import java.util.*

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


    fun tintToolbar(@Color from: Int, @Color to: Int)

    fun tintToolbar(@Color color: Int)


    fun changeName(text: String, cursor: Int)


    fun showRankDialog(check: Int)

    fun showColorDialog(@Color color: Int)

    fun showDateDialog(calendar: Calendar, resetVisible: Boolean)

    fun showTimeDialog(calendar: Calendar, dateList: List<String>)

    fun showConvertDialog()


    fun showSaveToast(success: Boolean)

}