package sgtmelon.scriptum.screen.ui.callback.main

import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Interface for communication [RankViewModel] with [RankFragment]
 */
interface IRankFragment : IRankBridge {

    fun setupToolbar()

    fun setupRecycler()

    fun bindList()

    fun onBindingToolbar(isClearEnable: Boolean, isAddEnable: Boolean)

    fun scrollTop()

    fun getEnterText(): String

    fun clearEnter(): String

    fun scrollToItem(simpleClick: Boolean, p: Int, list: MutableList<RankItem>)

    fun showRenameDialog(p: Int, name: String, nameList: List<String>)


    fun setList(list: List<RankItem>)

    fun notifyList(list: List<RankItem>)

    fun notifyList(list: List<RankItem>, startAnim: BooleanArray)

    fun notifyList(list: List<RankItem>, from: Int, to: Int)

}