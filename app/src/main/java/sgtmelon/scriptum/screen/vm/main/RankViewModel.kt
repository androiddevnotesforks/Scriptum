package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.extension.clearSpace
import sgtmelon.scriptum.extension.toUpperCase
import sgtmelon.scriptum.interactor.main.rank.IRankInteractor
import sgtmelon.scriptum.interactor.main.rank.RankInteractor
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.main.IRankViewModel

/**
 * ViewModel for [RankFragment]
 */
class RankViewModel(application: Application) : ParentViewModel<IRankFragment>(application),
        IRankViewModel {

    private val iInteractor: IRankInteractor = RankInteractor(context)

    private val itemList: MutableList<RankEntity> = ArrayList()
    private val nameList: List<String> get() = itemList.map { it.name.toUpperCase() }

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupToolbar()
            setupRecycler()
        }
    }

    override fun onUpdateData() {
        itemList.clearAndAdd(iInteractor.getList())

        callback?.apply {
            notifyDataSetChanged(itemList)
            bindList(itemList.size)
        }
    }

    override fun onUpdateToolbar() {
        callback?.apply {
            val enterName = getEnterText()
            val clearName = enterName.clearSpace().toUpperCase()

            bindToolbar(
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
        val enterName = callback?.getEnterText() ?: ""
        val clearName = enterName.clearSpace().toUpperCase()

        if (i != EditorInfo.IME_ACTION_DONE || enterName.isEmpty()) return false

        if (clearName.isNotEmpty() && !nameList.contains(clearName)) {
            onClickEnterAdd(simpleClick = true)
        }

        return true
    }

    override fun onClickEnterAdd(simpleClick: Boolean) {
        val name = callback?.clearEnter()?.clearSpace() ?: ""

        if (name.isEmpty()) return

        val p = if (simpleClick) itemList.size else 0
        itemList.add(p, iInteractor.insert(name))

        iInteractor.update(itemList)

        callback?.scrollToItem(simpleClick, itemList)
    }


    override fun onResultTouchClear(dragFrom: Int, dragTo: Int) {
        iInteractor.update(itemList)
        viewModelScope.launch { iInteractor.notifyBind() }

        callback?.notifyDataSetChanged(itemList)
    }

    override fun onResultTouchMove(from: Int, to: Int): Boolean {
        val item = itemList[from]

        itemList.removeAt(from)
        itemList.add(to, item)

        callback?.notifyItemMoved(from, to, itemList)

        return true
    }


    override fun onClickVisible(p: Int) {
        val item = itemList[p].apply { isVisible = !isVisible }

        viewModelScope.launch {
            iInteractor.update(item)
            iInteractor.notifyBind()
        }

        callback?.notifyVisible(p, item)
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

        iInteractor.update(itemList)
        viewModelScope.launch { iInteractor.notifyBind() }
    }

    override fun onClickCancel(p: Int) {
        iInteractor.delete(itemList[p])

        itemList.removeAt(p)

        iInteractor.update(itemList)
        viewModelScope.launch { iInteractor.notifyBind() }

        callback?.notifyItemRemoved(p, itemList)
    }

}