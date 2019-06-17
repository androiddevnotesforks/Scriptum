package sgtmelon.scriptum.screen.callback.main

import sgtmelon.scriptum.room.entity.RankItem
import sgtmelon.scriptum.screen.view.main.RankFragment
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Интерфейс для общения [RankViewModel] и [RankFragment]
 *
 * @author SerjantArbuz
 */
interface RankCallback {

    fun bindList(size: Int)

    fun bindToolbar(isClearEnable: Boolean, isAddEnable: Boolean)

    fun scrollTop()

    fun getEnterText(): String

    fun clearEnter(): String

    fun scrollToItem(simpleClick: Boolean, list: MutableList<RankItem>)

    fun showRenameDialog(p: Int, name: String, nameList: ArrayList<String>)

    fun notifyVisible(p: Int, item: RankItem)

    fun notifyVisible(startAnim: BooleanArray, list: MutableList<RankItem>)

    fun notifyDataSetChanged(list: MutableList<RankItem>)

    fun notifyItemChanged(p: Int, item: RankItem)

    fun notifyItemRemoved(p: Int, list: MutableList<RankItem>)

    fun notifyItemMoved(from: Int, to: Int, list: MutableList<RankItem>)

}