package sgtmelon.scriptum.app.screen.vm.main

import android.app.Application
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import sgtmelon.scriptum.app.control.touch.RankTouchControl
import sgtmelon.scriptum.app.model.RankModel
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.screen.callback.main.RankCallback
import sgtmelon.scriptum.app.screen.vm.ParentViewModel
import sgtmelon.scriptum.office.utils.AppUtils.clearAndAdd

/**
 * ViewModel для [RankFragment]
 *
 * @author SerjantArbuz
 */
class RankViewModel(application: Application) : ParentViewModel(application),
        RankTouchControl.Result {

    lateinit var callback: RankCallback

    var rankModel: RankModel = RankModel(ArrayList())
        private set

    fun onUpdateData() {
        rankModel = iRoomRepo.getRankModel()

        callback.apply {
            notifyDataSetChanged(rankModel.itemList)
            bindList(rankModel.size())
        }
    }

    fun onShowRenameDialog(p: Int) =
            callback.showRenameDialog(p, rankModel.itemList[p].name, ArrayList(rankModel.nameList))

    fun onRenameDialog(p: Int, name: String) {
        rankModel.set(p, name)

        val rankItem = rankModel.itemList[p]
        iRoomRepo.updateRank(rankItem)

        callback.apply {
            bindToolbar()
            notifyItemChanged(p, rankItem)
        }
    }

    fun onEditorClick(i: Int, name: String): Boolean {
        if (i != EditorInfo.IME_ACTION_DONE) return false

        if (!TextUtils.isEmpty(name) && !rankModel.nameList.contains(name)) {
            onClickAdd(simpleClick = true)
            return true
        }

        return false
    }

    fun onClickAdd(simpleClick: Boolean) {
        val name = callback.clearEnter()

        if (name.isEmpty()) return

        val p = if (simpleClick) rankModel.size() else 0

        val rankItem = RankItem(position = if (simpleClick) p else -1, name = name).apply {
            id = iRoomRepo.insertRank(p, rankItem = this)
        }

        rankModel.add(p, rankItem)

        callback.scrollToItem(simpleClick, rankModel.itemList)
    }

    override fun onResultTouchClear(dragFrom: Int, dragTo: Int) {
        iRoomRepo.apply {
            rankModel.itemList.clearAndAdd(updateRank(dragFrom, dragTo))
            notifyStatusBar()
        }

        callback.notifyDataSetChanged(rankModel.itemList)
    }

    override fun onResultTouchMove(from: Int, to: Int): Boolean {
        rankModel.move(from, to)
        callback.notifyItemMoved(from, to, rankModel.itemList)

        return true
    }

    fun onClickVisible(p: Int) {
        val rankItem = rankModel.itemList[p].apply { isVisible = !isVisible }

        iRoomRepo.apply {
            updateRank(rankItem)
            notifyStatusBar()
        }

        callback.notifyVisible(p, rankItem)
    }

    fun onLongClickVisible(p: Int) {
        val rankList = rankModel.itemList
        val startAnim = BooleanArray(rankList.size)

        val clickVisible = rankList[p].isVisible
        rankList.forEachIndexed { index, rankItem ->
            if (index == p || clickVisible != rankItem.isVisible) return@forEachIndexed

            rankItem.isVisible = !rankItem.isVisible
            startAnim[index] = true
        }

        callback.notifyVisible(startAnim, rankList)

        iRoomRepo.apply {
            updateRank(rankList)
            notifyStatusBar()
        }
    }

    fun onClickCancel(p: Int) {
        iRoomRepo.apply {
            deleteRank(rankModel.itemList[p].name, p)
            notifyStatusBar()
        }

        rankModel.remove(p)

        callback.notifyItemRemoved(p, rankModel.itemList)
    }

}