package sgtmelon.scriptum.presentation.screen.ui.callback.note.text

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.note.TextNoteViewModel
import java.util.*

/**
 * Interface for communication [TextNoteViewModel] with [TextNoteFragment]
 */
interface ITextNoteFragment : ITextNoteBridge {

    val isDialogOpen: Boolean

    fun hideKeyboard()


    /**
     * Setup elements for binding which is constants
     */
    fun setupBinding(@Theme theme: Int)

    fun setupToolbar(@Theme theme: Int, @Color color: Int)

    fun setupDialog(rankNameArray: Array<String>)

    fun setupEnter(iInputControl: IInputControl)


    fun onBindingLoad(isRankEmpty: Boolean)

    fun onBindingNote(item: NoteItem.Text)

    fun onBindingEdit(item: NoteItem.Text, isEditMode: Boolean)

    fun onBindingInput(item: NoteItem.Text, inputAccess: InputControl.Access)


    fun onPressBack(): Boolean

    fun tintToolbar(@Color from: Int, @Color to: Int)

    fun tintToolbar(@Color color: Int)

    fun setToolbarBackIcon(isCancel: Boolean, needAnim: Boolean)

    fun focusOnEdit(isCreate: Boolean)

    fun changeName(text: String, cursor: Int)

    fun changeText(text: String, cursor: Int)


    fun showRankDialog(check: Int)

    fun showColorDialog(@Color color: Int, @Theme theme: Int)

    fun showDateDialog(calendar: Calendar, resetVisible: Boolean)

    fun showTimeDialog(calendar: Calendar, dateList: List<String>)

    fun showConvertDialog()


    fun showSaveToast(success: Boolean)

}