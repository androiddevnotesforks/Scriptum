package sgtmelon.scriptum.app.screen.main.rank

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.control.touch.RankTouchControl
import sgtmelon.scriptum.app.model.RankModel
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.room.RoomDb

/**
 * ViewModel для [RankFragment]
 *
 * @author SerjantArbuz
 * @version 1.0
 */
class RankViewModel(application: Application) : AndroidViewModel(application),
        RankTouchControl.Result {

    private val context: Context = application.applicationContext
    private val iRoomRepo: IRoomRepo = RoomRepo.getInstance(context)

    lateinit var callback: RankCallback

    var rankModel: RankModel = RankModel(ArrayList())
        private set

    fun onUpdateData() {
        rankModel = iRoomRepo.getRankModel()

        callback.notifyDataSetChanged(rankModel.itemList)
        callback.bindList(rankModel.size())
    }

    fun onShowRenameDialog(p: Int) =
            callback.showRenameDialog(p, rankModel.itemList[p].name, ArrayList(rankModel.nameList))

    fun onRenameDialog(p: Int, name: String) {
        rankModel.set(p, name)

        val rankItem = rankModel.itemList[p]

        val db = RoomDb.getInstance(context)
        db.getRankDao().update(rankItem)
        db.close()

        callback.bindToolbar()
        callback.notifyItemChanged(p, rankItem)
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

        val rankItem = RankItem(if (simpleClick) p else -1, name)
        rankItem.id = iRoomRepo.insertRank(p, rankItem)

        rankModel.add(p, rankItem)

        callback.scrollToItem(simpleClick, rankModel.itemList)
    }

    override fun onResultTouchClear(dragFrom: Int, dragTo: Int) { // TODO: 03.02.2019 ошибка сортировки
        iRoomRepo.apply {
            rankModel.apply {
                itemList.clear()
                itemList.addAll(updateRank(dragFrom, dragTo))
            }
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
        val rankItem = rankModel.itemList[p]
        rankItem.isVisible = !rankItem.isVisible

        val db = RoomDb.getInstance(context)
        db.getRankDao().update(rankItem)
        iRoomRepo.notifyStatusBar()
        db.close()

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

        val db = RoomDb.getInstance(context)
        db.getRankDao().update(rankList)
        iRoomRepo.notifyStatusBar()
        db.close()
    }

    fun onClickCancel(p: Int) {
        iRoomRepo.deleteRank(rankModel.itemList[p].name, p)
        iRoomRepo.notifyStatusBar()

        rankModel.remove(p)

        callback.notifyItemRemoved(p, rankModel.itemList)
    }

}