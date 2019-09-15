package sgtmelon.scriptum.screen.ui.callback.main

import sgtmelon.scriptum.control.bind.IBindBridge
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Interface for communication [RankViewModel] with [RankFragment]
 */
interface IRankFragment : IBindBridge.Notify {

    fun setupToolbar()

    fun setupRecycler()

    fun bindList(size: Int)

    fun bindToolbar(isClearEnable: Boolean, isAddEnable: Boolean)

    fun scrollTop()

    fun getEnterText(): String

    fun clearEnter(): String

    fun scrollToItem(simpleClick: Boolean, list: MutableList<RankEntity>)

    fun showRenameDialog(p: Int, name: String, nameList: List<String>)

    fun notifyVisible(p: Int, item: RankEntity)

    fun notifyVisible(startAnim: BooleanArray, list: MutableList<RankEntity>)

    fun notifyDataSetChanged(list: MutableList<RankEntity>)

    fun notifyItemChanged(p: Int, item: RankEntity)

    fun notifyItemRemoved(p: Int, list: MutableList<RankEntity>)

    fun notifyItemMoved(from: Int, to: Int, list: MutableList<RankEntity>)

}