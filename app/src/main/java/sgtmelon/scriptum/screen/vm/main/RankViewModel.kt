package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.extension.clearSpace
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.main.IRankViewModel

/**
 * ViewModel for [RankFragment]
 *
 * @author SerjantArbuz
 */
class RankViewModel(application: Application) : ParentViewModel<IRankFragment>(application),
        IRankViewModel {

    private val iRankRepo = RankRepo.getInstance(context)

    private val rankList: MutableList<RankEntity> = ArrayList()
    private val nameList: List<String> get() = rankList.map { it.name.toUpperCase() }

    override fun onUpdateData() {
        rankList.clearAndAdd(iRankRepo.get())

        callback?.apply {
            notifyDataSetChanged(rankList)
            bindList(rankList.size)
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
        callback?.showRenameDialog(p, rankList[p].name, ArrayList(nameList))
    }

    override fun onRenameDialog(p: Int, name: String) {
        rankList[p].name = name

        onUpdateToolbar()

        rankList[p].let {
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

        val p = if (simpleClick) rankList.size else 0
        val rankEntity = RankEntity(name = name).apply {
            id = iRankRepo.insert(rankEntity = this)
        }

        rankList.add(p, rankEntity)
        iRankRepo.update(rankList)

        callback?.scrollToItem(simpleClick, rankList)
    }


    override fun onResultTouchClear(dragFrom: Int, dragTo: Int) {
        iRankRepo.update(rankList)
        viewModelScope.launch { iRankRepo.notifyBind() }

        callback?.notifyDataSetChanged(rankList)
    }

    override fun onResultTouchMove(from: Int, to: Int): Boolean {
        val item = rankList[from]

        rankList.removeAt(from)
        rankList.add(to, item)

        callback?.notifyItemMoved(from, to, rankList)

        return true
    }


    override fun onClickVisible(p: Int) {
        val rankEntity = rankList[p].apply { isVisible = !isVisible }

        iRankRepo.update(rankEntity)
        viewModelScope.launch { iRankRepo.notifyBind() }

        callback?.notifyVisible(p, rankEntity)
    }

    override fun onLongClickVisible(p: Int) {
        val startAnim = BooleanArray(rankList.size)

        rankList.forEachIndexed { i, item ->
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

        callback?.notifyVisible(startAnim, rankList)

        iRankRepo.update(rankList)
        viewModelScope.launch { iRankRepo.notifyBind() }
    }

    override fun onClickCancel(p: Int) {
        iRankRepo.delete(rankList[p].name)

        rankList.removeAt(p)

        iRankRepo.update(rankList)
        viewModelScope.launch { iRankRepo.notifyBind() }
        
        callback?.notifyItemRemoved(p, rankList)
    }

}