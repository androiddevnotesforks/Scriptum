package sgtmelon.scriptum.app.ui.main.rank

import sgtmelon.scriptum.app.model.item.RankItem

/**
 * Интерфейс для общения [RankViewModel] и [RankFragment]
 */
interface RankCallback {

    fun bindList(size: Int)

    fun bindToolbar()

    fun clearEnter(): String

    fun scrollTop()

    fun addItem(simpleClick: Boolean, list: MutableList<RankItem>)

    fun notifyVisible(p: Int, item: RankItem)

    fun notifyVisible(startAnim: BooleanArray, list: MutableList<RankItem>)

    fun notifyDataSetChanged(list: MutableList<RankItem>)

    fun notifyItemChanged(p: Int, item: RankItem)

    fun notifyItemRemoved(p: Int, list: MutableList<RankItem>)

    fun notifyItemMoved(from: Int, to: Int, list: MutableList<RankItem>)

}