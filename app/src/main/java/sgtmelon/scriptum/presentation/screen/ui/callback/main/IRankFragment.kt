package sgtmelon.scriptum.presentation.screen.ui.callback.main

import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IRankViewModel

/**
 * Interface for communication [IRankViewModel] with [RankFragment].
 */
interface IRankFragment : SystemReceiver.Bridge.Bind {

    val openState: OpenState?

    fun hideKeyboard()

    fun setupToolbar()

    fun setupRecycler()

    fun setupDialog()


    /**
     * Calls before load data inside list.
     */
    fun prepareForLoad()

    fun showProgress()

    fun hideEmptyInfo()


    fun onBindingList()

    fun onBindingToolbar(isClearEnable: Boolean, isAddEnable: Boolean)

    fun scrollTop()

    fun showSnackbar()

    fun dismissSnackbar(withCallback: Boolean = true)


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