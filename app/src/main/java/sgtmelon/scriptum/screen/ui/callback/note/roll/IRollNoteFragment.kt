package sgtmelon.scriptum.screen.ui.callback.note.roll

import sgtmelon.scriptum.control.input.IInputControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel
import java.util.*

/**
 * Interface for communication [RollNoteViewModel] with [RollNoteFragment]
 */
interface IRollNoteFragment : IRollNoteBridge {

    val isDialogOpen: Boolean

    fun hideKeyboard()


    /**
     * Setup elements for binding which is constants
     */
    fun setupBinding(@Theme theme: Int)

    fun setupToolbar(@Theme theme: Int, @Color color: Int)

    fun setupDialog(rankNameArray: Array<String>)

    fun setupEnter(iInputControl: IInputControl)

    fun setupRecycler(iInputControl: IInputControl)

    fun setupProgress()


    fun onBindingLoad(rankEmpty: Boolean)

    fun onBindingEdit(editMode: Boolean, item: NoteItem)

    fun onBingingNote(item: NoteItem)

    fun onBindingEnter()

    fun onBindingInput(item: NoteItem, inputAccess: InputControl.Access)


    fun onPressBack(): Boolean

    fun tintToolbar(@Color from: Int, @Color to: Int)

    fun tintToolbar(@Color color: Int)

    fun changeToolbarIcon(drawableOn: Boolean, needAnim: Boolean)

    fun focusOnEdit(isCreate: Boolean)

    fun changeName(text: String, cursor: Int)

    fun onFocusEnter()

    fun getEnterText(): String

    fun clearEnterText()


    fun scrollToItem(simpleClick: Boolean, p: Int, list: MutableList<RollItem>)

    fun changeCheckToggle(state: Boolean)

    fun updateNoteState(noteState: NoteState)

    fun updateProgress(progress: Int, max: Int)

    fun setList(list: List<RollItem>)

    fun notifyDataSetChanged(list: List<RollItem>)

    fun notifyItemChanged(list: List<RollItem>, p: Int, cursor: Int?)

    fun notifyItemMoved(list: List<RollItem>, from: Int, to: Int)

    fun notifyItemInserted(list: List<RollItem>, p: Int, cursor: Int)

    fun notifyItemRemoved(list: List<RollItem>, p: Int)

    fun notifyItemRemoved(p: Int)


    fun showRankDialog(check: Int)

    fun showColorDialog(@Color color: Int, @Theme theme: Int)

    fun showDateDialog(calendar: Calendar, resetVisible: Boolean)

    fun showTimeDialog(calendar: Calendar, dateList: List<String>)

    fun showConvertDialog()


    fun showSaveToast(success: Boolean)

}