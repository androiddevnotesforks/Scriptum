package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.extension.clearSpace
import sgtmelon.scriptum.model.RankModel
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

    private var rankModel: RankModel = RankModel(ArrayList())

    override fun onUpdateData() {
        rankModel = iRankRepo.getRankModel()

        callback?.apply {
            notifyDataSetChanged(rankModel.itemList)
            bindList(rankModel.size())
        }
    }

    override fun onUpdateToolbar() {
        callback?.apply {
            val enterName = getEnterText()
            val clearName = enterName.clearSpace().toUpperCase()

            bindToolbar(
                    isClearEnable = enterName.isNotEmpty(),
                    isAddEnable = clearName.isNotEmpty() && !rankModel.nameList.contains(clearName)
            )
        }
    }

    override fun onShowRenameDialog(p: Int) {
        callback?.showRenameDialog(p, rankModel.itemList[p].name, ArrayList(rankModel.nameList))
    }

    override fun onRenameDialog(p: Int, name: String) {
        rankModel.set(p, name)

        onUpdateToolbar()

        rankModel.itemList[p].let {
            iRankRepo.updateRank(it)
            callback?.notifyItemChanged(p, it)
        }
    }

    override fun onClickCancel() = callback?.clearEnter() ?: ""

    override fun onEditorClick(i: Int): Boolean {
        val enterName = callback?.getEnterText() ?: ""
        val clearName = enterName.clearSpace().toUpperCase()

        if (i != EditorInfo.IME_ACTION_DONE || enterName.isEmpty()) return false

        if (clearName.isNotEmpty() && !rankModel.nameList.contains(clearName)) {
            onClickAdd(simpleClick = true)
        }

        return true
    }

    override fun onClickAdd(simpleClick: Boolean) {
        val name = callback?.clearEnter()?.clearSpace() ?: ""

        if (name.isEmpty()) return

        val p = if (simpleClick) rankModel.size() else 0
        val rankEntity = RankEntity(position = if (simpleClick) p else -1, name = name).apply {
            id = iRankRepo.insertRank(p, rankEntity = this)
        }

        rankModel.add(p, rankEntity)

        callback?.scrollToItem(simpleClick, rankModel.itemList)
    }

    override fun onResultTouchClear(dragFrom: Int, dragTo: Int) {
        rankModel.itemList.clearAndAdd(iRankRepo.updateRank(dragFrom, dragTo))
        viewModelScope.launch { iRankRepo.notifyBind() }

        callback?.notifyDataSetChanged(rankModel.itemList)
    }

    override fun onResultTouchMove(from: Int, to: Int): Boolean {
        rankModel.move(from, to)
        callback?.notifyItemMoved(from, to, rankModel.itemList)

        return true
    }

    override fun onClickVisible(p: Int) {
        val rankEntity = rankModel.itemList[p].apply { isVisible = !isVisible }

        iRankRepo.updateRank(rankEntity)
        viewModelScope.launch { iRankRepo.notifyBind() }

        callback?.notifyVisible(p, rankEntity)
    }

    override fun onLongClickVisible(p: Int) {
        val rankList = rankModel.itemList
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

        iRankRepo.updateRank(rankList)
        viewModelScope.launch { iRankRepo.notifyBind() }
    }

    override fun onClickCancel(p: Int) {
        iRankRepo.deleteRank(rankModel.itemList[p].name, p)
        viewModelScope.launch { iRankRepo.notifyBind() }

        rankModel.remove(p)

        callback?.notifyItemRemoved(p, rankModel.itemList)
    }

}