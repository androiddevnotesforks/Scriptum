package sgtmelon.scriptum.screen.ui.callback.note.text

import sgtmelon.scriptum.control.input.IInputControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel
import java.util.*

/**
 * Interface for communication [TextNoteViewModel] with [TextNoteFragment]
 */
interface ITextNoteFragment : ITextNoteBridge {

    /**
     * Setup elements for binding which is constants
     */
    fun setupBinding(@Theme theme: Int)

    fun setupToolbar(@Theme theme: Int, @Color color: Int)

    fun setupDialog(rankNameArray: Array<String>)

    fun setupEnter(iInputControl: IInputControl)


    fun onBindingLoad(rankEmpty: Boolean)

    fun onBindingNote(item: NoteItem)

    fun onBindingEdit(editMode: Boolean, item: NoteItem)

    fun onBindingInput(item: NoteItem, inputAccess: InputControl.Access)


    fun onPressBack(): Boolean

    fun tintToolbar(@Color from: Int, @Color to: Int)

    fun tintToolbar(@Color color: Int)

    fun changeToolbarIcon(drawableOn: Boolean, needAnim: Boolean)

    fun focusOnEdit()

    fun changeName(text: String, cursor: Int)

    fun changeText(text: String, cursor: Int)

    fun hideKeyboard()


    fun showRankDialog(check: Int)

    fun showColorDialog(@Color color: Int, @Theme theme: Int)

    fun showDateDialog(calendar: Calendar, resetVisible: Boolean)

    fun showTimeDialog(calendar: Calendar, dateList: List<String>)

    fun showConvertDialog()

}