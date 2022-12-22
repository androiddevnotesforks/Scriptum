package sgtmelon.scriptum.infrastructure.screen.note.roll

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragment

/**
 * Interface for communication [RollNoteViewModel] with [RollNoteFragmentImpl].
 */
interface RollNoteFragment : ParentNoteFragment<NoteItem.Roll> {

    fun setTouchAction(inAction: Boolean)

    fun setupEnter(inputControl: IInputControl)

    fun setupRecycler(inputControl: IInputControl)

    /**
     * Need hide toolbar visible icon before information completely load.
     */
    fun showToolbarVisibleIcon(isShow: Boolean)


    fun onBindingLoad(isRankEmpty: Boolean)

    fun onBindingInfo(isListEmpty: Boolean, isListHide: Boolean)

    fun onBindingEnter()


    fun setToolbarVisibleIcon(isVisible: Boolean, needAnim: Boolean)

    fun animateInfoVisible(isVisible: Boolean? = null)

    fun focusOnEdit(isCreate: Boolean)

    fun onFocusEnter()

    fun getEnterText(): String

    fun clearEnterText()


    fun scrollToItem(simpleClick: Boolean, p: Int, list: MutableList<RollItem>)

    fun updateNoteState(isEdit: Boolean, noteState: NoteState?)

    fun updateProgress(progress: Int, max: Int)

    fun setList(list: List<RollItem>)

    fun notifyDataSetChanged(list: List<RollItem>)

    fun notifyDataRangeChanged(list: List<RollItem>)

    fun notifyItemChanged(list: List<RollItem>, p: Int, cursor: Int? = null)

    fun notifyItemMoved(list: List<RollItem>, from: Int, to: Int)

    fun notifyItemInserted(list: List<RollItem>, p: Int, cursor: Int? = null)

    fun notifyItemRemoved(list: List<RollItem>, p: Int)

}