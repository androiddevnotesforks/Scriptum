package sgtmelon.scriptum.infrastructure.screen.note.roll

import sgtmelon.scriptum.cleanup.domain.model.item.RollItem

/**
 * Interface for communication [RollNoteViewModel] with [RollNoteFragmentImpl].
 */
interface RollNoteFragment {


    fun onBindingInfo(isListEmpty: Boolean, isListHide: Boolean)

    fun animateInfoVisible(isVisible: Boolean? = null)


    fun scrollToItem(toBottom: Boolean, p: Int, list: MutableList<RollItem>)

    fun setList(list: List<RollItem>)

    fun notifyDataSetChanged(list: List<RollItem>)

    fun notifyDataRangeChanged(list: List<RollItem>)

    fun notifyItemChanged(list: List<RollItem>, p: Int, cursor: Int? = null)

    fun notifyItemMoved(list: List<RollItem>, from: Int, to: Int)

    fun notifyItemInserted(list: List<RollItem>, p: Int, cursor: Int? = null)

    fun notifyItemRemoved(list: List<RollItem>, p: Int)

}