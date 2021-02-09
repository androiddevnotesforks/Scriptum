package sgtmelon.scriptum.presentation.screen.ui.callback.note

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.note.IRollNoteViewModel

/**
 * Interface for communication [IRollNoteViewModel] with [RollNoteFragment].
 */
interface IRollNoteFragment : IParentNoteFragment<NoteItem.Roll> {

    fun setTouchAction(inAction: Boolean)


    /**
     * Setup elements for binding which is constants
     */
    fun setupBinding()

    fun setupToolbar(@Color color: Int)

    fun setupDialog(rankNameArray: Array<String>)

    fun setupEnter(inputControl: IInputControl)

    fun setupRecycler(inputControl: IInputControl, isFirstRun: Boolean)

    fun setupProgress()

    /**
     * Need hide toolbar visible icon before information completely load.
     */
    fun showToolbarVisibleIcon(isShow: Boolean)


    fun onBindingLoad(isRankEmpty: Boolean)

    fun onBindingInfo(isListEmpty: Boolean, isListHide: Boolean)

    fun onBindingEnter()


    fun onPressBack(): Boolean

    fun setToolbarBackIcon(isCancel: Boolean, needAnim: Boolean)

    fun setToolbarVisibleIcon(isVisible: Boolean, needAnim: Boolean)

    fun animateInfoVisible(isVisible: Boolean? = null)

    fun focusOnEdit(isCreate: Boolean)

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

}