package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.extension.clearSpace
import sgtmelon.scriptum.extension.toUpperCase
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo
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

    private val iRankRepo: IRankRepo = RankRepo(context)

    private val itemList: MutableList<RankEntity> = ArrayList()
    private val nameList: List<String> get() = itemList.map { it.name.toUpperCase() }

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupToolbar()
            setupRecycler()
        }
    }

    override fun onUpdateData() {
        itemList.clearAndAdd(iRankRepo.get())

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
        itemList[p].name = name

        onUpdateToolbar()

        itemList[p].let {
            iRankRepo.update(it)
            callback?.notifyItemChanged(p, it)
        }
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
        val rankEntity = RankEntity(name = name).apply {
            id = iRankRepo.insert(rankEntity = this)
        }

        itemList.add(p, rankEntity)
        iRankRepo.update(itemList)

        callback?.scrollToItem(simpleClick, itemList)
    }


    override fun onResultTouchClear(dragFrom: Int, dragTo: Int) {
        iRankRepo.update(itemList)
        viewModelScope.launch { iRankRepo.notifyBind() }

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
        val rankEntity = itemList[p].apply { isVisible = !isVisible }

        iRankRepo.update(rankEntity)
        viewModelScope.launch { iRankRepo.notifyBind() }

        callback?.notifyVisible(p, rankEntity)
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

        iRankRepo.update(itemList)
        viewModelScope.launch { iRankRepo.notifyBind() }
    }

    override fun onClickCancel(p: Int) {
        iRankRepo.delete(itemList[p])

        itemList.removeAt(p)

        iRankRepo.update(itemList)
        viewModelScope.launch { iRankRepo.notifyBind() }
        
        callback?.notifyItemRemoved(p, itemList)
    }

}