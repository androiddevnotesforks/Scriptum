package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.extension.clearSpace
import sgtmelon.scriptum.extension.swap
import sgtmelon.scriptum.extension.toUpperCase
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.interactor.main.RankInteractor
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.main.IRankViewModel

/**
 * ViewModel for [RankFragment]
 */
class RankViewModel(application: Application) : ParentViewModel<IRankFragment>(application),
        IRankViewModel {

    private val iInteractor: IRankInteractor by lazy { RankInteractor(context) }
    private val iBindInteractor: IBindInteractor by lazy { BindInteractor(context) }

    private val itemList: MutableList<RankItem> = ArrayList()
    private val nameList: List<String> get() = itemList.map { it.name.toUpperCase() }

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupToolbar()
            setupRecycler()
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { iInteractor.onDestroy() }


    override fun onUpdateData() {
        itemList.clearAndAdd(iInteractor.getList())

        callback?.apply {
            notifyDataSetChanged(itemList)
            bindList()
        }
    }

    override fun onUpdateToolbar() {
        callback?.apply {
            val enterName = getEnterText()
            val clearName = enterName.clearSpace().toUpperCase()

            onBindingToolbar(
                    isClearEnable = enterName.isNotEmpty(),
                    isAddEnable = clearName.isNotEmpty() && !nameList.contains(clearName)
            )
        }
    }

    override fun onShowRenameDialog(p: Int) {
        callback?.showRenameDialog(p, itemList[p].name, nameList)
    }

    override fun onRenameDialog(p: Int, name: String) {
        val item = itemList[p].apply { this.name = name }

        viewModelScope.launch { iInteractor.update(item) }

        onUpdateToolbar()
        callback?.notifyItemChanged(p, item)
    }


    override fun onClickEnterCancel() = callback?.clearEnter() ?: ""

    override fun onEditorClick(i: Int): Boolean {
        if (i != EditorInfo.IME_ACTION_DONE) return false

        val name = callback?.getEnterText()?.clearSpace()?.toUpperCase() ?: ""

        if (name.isEmpty()) return false

        if (!nameList.contains(name)) {
            onClickEnterAdd(simpleClick = true)
        }

        return true
    }

    override fun onClickEnterAdd(simpleClick: Boolean) {
        val name = callback?.clearEnter()?.clearSpace() ?: ""

        if (name.isEmpty()) return

        val p = if (simpleClick) itemList.size else 0
        itemList.add(p, iInteractor.insert(name))

        iInteractor.updatePosition(itemList)

        callback?.scrollToItem(simpleClick, p, itemList)
    }

    override fun onClickVisible(p: Int) {
        val item = itemList[p].apply { isVisible = !isVisible }

        callback?.notifyVisible(p, item)

        viewModelScope.launch {
            iInteractor.update(item)
            iBindInteractor.notifyNoteBind(callback)
        }
    }

    override fun onLongClickVisible(p: Int) {
        val startAnim = BooleanArray(itemList.size)

        itemList.forEachIndexed { i, item ->
            if (i == p) {
                if (!item.isVisible) {
                    item.isVisible = true
                    startAnim[i] = true
                }
            } else {
                if (item.isVisible) {
                    item.isVisible = false
                    startAnim[i] = true
                }
            }
        }

        callback?.notifyVisible(startAnim, itemList)

        viewModelScope.launch {
            iInteractor.update(itemList)
            iBindInteractor.notifyNoteBind(callback)
        }
    }

    override fun onClickCancel(p: Int) {
        iInteractor.delete(itemList[p])

        itemList.removeAt(p)

        iInteractor.updatePosition(itemList)
        viewModelScope.launch { iBindInteractor.notifyNoteBind(callback) }

        callback?.notifyItemRemoved(p, itemList)
    }


    override fun onTouchMove(from: Int, to: Int): Boolean {
        itemList.swap(from, to)

        callback?.notifyItemMoved(from, to, itemList)

        return true
    }

    override fun onTouchMoveResult() {
        iInteractor.updatePosition(itemList)
        callback?.notifyDataSetChanged(itemList)
    }

}