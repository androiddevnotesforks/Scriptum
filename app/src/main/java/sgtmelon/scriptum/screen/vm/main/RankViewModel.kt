package sgtmelon.scriptum.screen.vm.main

import android.app.Application
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.control.touch.RankTouchControl
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.model.RankModel
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.callback.main.IRankFragment
import sgtmelon.scriptum.screen.view.main.RankFragment
import sgtmelon.scriptum.screen.vm.ParentViewModel

/**
 * ViewModel для [RankFragment]
 *
 * @author SerjantArbuz
 */
class RankViewModel(application: Application) : ParentViewModel(application),
        RankTouchControl.Result {

    lateinit var callback: IRankFragment

    private val iRankRepo = RankRepo.getInstance(context)

    private var rankModel: RankModel = RankModel(ArrayList())

    fun onUpdateData() {
        rankModel = iRankRepo.getRankModel()

        callback.apply {
            notifyDataSetChanged(rankModel.itemList)
            bindList(rankModel.size())
        }
    }

    fun onUpdateToolbar() = with(callback) {
        val name = getEnterText()
        bindToolbar(
                isClearEnable = name.isNotEmpty(),
                isAddEnable = name.isNotEmpty() && !rankModel.nameList.contains(name)
        )
    }

    fun onShowRenameDialog(p: Int) =
            callback.showRenameDialog(p, rankModel.itemList[p].name, ArrayList(rankModel.nameList))

    fun onRenameDialog(p: Int, name: String) {
        rankModel.set(p, name)

        onUpdateToolbar()

        rankModel.itemList[p].let {
            iRankRepo.updateRank(it)
            callback.notifyItemChanged(p, it)
        }
    }

    fun onClickCancel() = callback.clearEnter()

    fun onEditorClick(i: Int): Boolean {
        val name = callback.getEnterText()

        if (i != EditorInfo.IME_ACTION_DONE || name.isEmpty()) return false

        if (name.isNotEmpty() && !rankModel.nameList.contains(name)) {
            onClickAdd(simpleClick = true)
            return true
        }

        return false
    }

    fun onClickAdd(simpleClick: Boolean) {
        val name = callback.clearEnter()

        if (name.isEmpty()) return

        val p = if (simpleClick) rankModel.size() else 0
        val rankEntity = RankEntity(position = if (simpleClick) p else -1, name = name).apply {
            id = iRankRepo.insertRank(p, rankEntity = this)
        }

        rankModel.add(p, rankEntity)

        callback.scrollToItem(simpleClick, rankModel.itemList)
    }

    override fun onResultTouchClear(dragFrom: Int, dragTo: Int) {
        rankModel.itemList.clearAndAdd(iRankRepo.updateRank(dragFrom, dragTo))
        viewModelScope.launch { iRankRepo.notifyBind() }

        callback.notifyDataSetChanged(rankModel.itemList)
    }

    override fun onResultTouchMove(from: Int, to: Int): Boolean {
        rankModel.move(from, to)
        callback.notifyItemMoved(from, to, rankModel.itemList)

        return true
    }

    fun onClickVisible(p: Int) {
        val rankEntity = rankModel.itemList[p].apply { isVisible = !isVisible }

        iRankRepo.updateRank(rankEntity)
        viewModelScope.launch { iRankRepo.notifyBind() }

        callback.notifyVisible(p, rankEntity)
    }

    fun onLongClickVisible(p: Int) {
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

        callback.notifyVisible(startAnim, rankList)

        iRankRepo.updateRank(rankList)
        viewModelScope.launch { iRankRepo.notifyBind() }
    }

    fun onClickCancel(p: Int) {
        iRankRepo.deleteRank(rankModel.itemList[p].name, p)
        viewModelScope.launch { iRankRepo.notifyBind() }

        rankModel.remove(p)

        callback.notifyItemRemoved(p, rankModel.itemList)
    }

}