package sgtmelon.scriptum.infrastructure.screen.main.rank

import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.infrastructure.model.state.OpenState

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

    fun dismissSnackbar()


    fun getEnterText(): String

    fun clearEnter(): String

    fun scrollToItem(list: List<RankItem>, p: Int, addToBottom: Boolean)

    fun showRenameDialog(p: Int, name: String, nameList: List<String>)


    fun setList(list: List<RankItem>)

    fun notifyList(list: List<RankItem>)

    fun notifyItemMoved(list: List<RankItem>, from: Int, to: Int)

    fun notifyItemInsertedScroll(list: List<RankItem>, p: Int)

}