package sgtmelon.scriptum.screen.callback.main

import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.screen.view.main.RankFragment
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Интерфейс для общения [RankViewModel] и [RankFragment]
 *
 * @author SerjantArbuz
 */
interface RankCallback {

    fun bindList(size: Int)

    fun bindToolbar()

    fun scrollTop()

    fun clearEnter(): String

    fun scrollToItem(simpleClick: Boolean, list: MutableList<RankItem>)

    fun showRenameDialog(p: Int, name: String, listName: ArrayList<String>)

    fun notifyVisible(p: Int, item: RankItem)

    fun notifyVisible(startAnim: BooleanArray, list: MutableList<RankItem>)

    fun notifyDataSetChanged(list: MutableList<RankItem>)

    fun notifyItemChanged(p: Int, item: RankItem)

    fun notifyItemRemoved(p: Int, list: MutableList<RankItem>)

    fun notifyItemMoved(from: Int, to: Int, list: MutableList<RankItem>)

}