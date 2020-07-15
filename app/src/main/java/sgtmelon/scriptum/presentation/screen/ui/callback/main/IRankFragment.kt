package sgtmelon.scriptum.presentation.screen.ui.callback.main

import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IRankViewModel

/**
 * Interface for communication [IRankViewModel] with [RankFragment].
 */
interface IRankFragment : BindControl.NoteBridge.NotifyAll {

    val openState: OpenState?

    fun hideKeyboard()

    fun setupToolbar()

    fun setupRecycler()


    /**
     * Calls before load data inside list.
     */
    fun beforeLoad()

    fun showProgress()


    fun onBindingList()

    fun onBindingToolbar(isClearEnable: Boolean, isAddEnable: Boolean)

    fun scrollTop()

    fun showSnackbar(@Theme theme: Int)

    fun dismissSnackbar()


    fun getEnterText(): String

    fun clearEnter(): String

    fun scrollToItem(list: MutableList<RankItem>, p: Int, simpleClick: Boolean)

    fun showRenameDialog(p: Int, name: String, nameList: List<String>)


    fun setList(list: List<RankItem>)

    fun notifyList(list: List<RankItem>)

    fun notifyDataSetChanged(list: List<RankItem>, startAnim: BooleanArray)

    fun notifyItemChanged(list: List<RankItem>, p: Int)

    fun notifyItemRemoved(list: List<RankItem>, p: Int)

    fun notifyItemMoved(list: List<RankItem>, from: Int, to: Int)

    fun notifyItemInsertedScroll(list: List<RankItem>, p: Int)

}