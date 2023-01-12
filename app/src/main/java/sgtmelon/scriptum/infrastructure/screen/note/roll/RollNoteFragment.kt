package sgtmelon.scriptum.infrastructure.screen.note.roll

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragment

/**
 * Interface for communication [RollNoteViewModel] with [RollNoteFragmentImpl].
 */
interface RollNoteFragment : ParentNoteFragment<NoteItem.Roll> {

    fun onBindingLoad()

    fun onBindingInfo(isListEmpty: Boolean, isListHide: Boolean)

    fun setToolbarVisibleIcon(isVisible: Boolean, needAnim: Boolean)

    fun animateInfoVisible(isVisible: Boolean? = null)

    fun focusOnEdit(isCreate: Boolean)

    fun onFocusEnter()

    fun getEnterText(): String

    fun clearEnterText()


    fun scrollToItem(toBottom: Boolean, p: Int, list: MutableList<RollItem>)

    fun updateNoteState(isEdit: Boolean, state: NoteState?)

    fun updateProgress(progress: Int, max: Int)

    fun setList(list: List<RollItem>)

    fun notifyDataSetChanged(list: List<RollItem>)

    fun notifyDataRangeChanged(list: List<RollItem>)

    fun notifyItemChanged(list: List<RollItem>, p: Int, cursor: Int? = null)

    fun notifyItemMoved(list: List<RollItem>, from: Int, to: Int)

    fun notifyItemInserted(list: List<RollItem>, p: Int, cursor: Int? = null)

    fun notifyItemRemoved(list: List<RollItem>, p: Int)

}