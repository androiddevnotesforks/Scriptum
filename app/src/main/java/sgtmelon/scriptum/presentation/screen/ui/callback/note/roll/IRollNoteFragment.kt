package sgtmelon.scriptum.presentation.screen.ui.callback.note.roll

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.note.RollNoteViewModel
import java.util.*

/**
 * Interface for communication [RollNoteViewModel] with [RollNoteFragment].
 */
interface IRollNoteFragment : IRollNoteBridge {

    val openState: OpenState

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

    /**
     * Need hide toolbar visible icon before information completely load.
     */
    fun showToolbarVisibleIcon(isShow: Boolean)


    fun onBindingLoad(rankEmpty: Boolean)

    fun onBindingEdit(editMode: Boolean, item: NoteItem)

    fun onBingingNote(item: NoteItem)

    fun onBindingEnter()

    fun onBindingInput(item: NoteItem, inputAccess: InputControl.Access)


    fun onPressBack(): Boolean

    fun tintToolbar(@Color from: Int, @Color to: Int)

    fun tintToolbar(@Color color: Int)

    fun setToolbarBackIcon(isCancel: Boolean, needAnim: Boolean)

    fun setToolbarVisibleIcon(isVisible: Boolean, needAnim: Boolean)

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

    fun notifyDataRangeChanged(list: List<RollItem>)

    fun notifyItemChanged(list: List<RollItem>, p: Int, cursor: Int? = null)

    fun notifyItemMoved(list: List<RollItem>, from: Int, to: Int)

    fun notifyItemInserted(list: List<RollItem>, p: Int, cursor: Int? = null)

    fun notifyItemRemoved(list: List<RollItem>, p: Int)


    fun showRankDialog(check: Int)

    fun showColorDialog(@Color color: Int, @Theme theme: Int)

    fun showDateDialog(calendar: Calendar, resetVisible: Boolean)

    fun showTimeDialog(calendar: Calendar, dateList: List<String>)

    fun showConvertDialog()


    fun showSaveToast(success: Boolean)

}