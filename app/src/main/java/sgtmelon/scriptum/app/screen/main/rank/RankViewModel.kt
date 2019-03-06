package sgtmelon.scriptum.app.screen.main.rank

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.control.touch.RankTouchControl
import sgtmelon.scriptum.app.model.RankRepo
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.room.RoomDb

/**
 * ViewModel для [RankFragment]
 */
class RankViewModel(application: Application) : AndroidViewModel(application),
        RankTouchControl.Result {

    private val context: Context = application.applicationContext
    private val iRoomRepo: IRoomRepo = RoomRepo.getInstance(context)

    lateinit var callback: RankCallback

    var rankRepo: RankRepo = RankRepo(ArrayList(), ArrayList())
        private set

    fun onUpdateData() {
        val db = RoomDb.provideDb(context)
        rankRepo = db.daoRank().get()
        db.close()

        callback.notifyDataSetChanged(rankRepo.listRank)
        callback.bindList(rankRepo.size())
    }

    fun onShowRenameDialog(p: Int) =
            callback.showRenameDialog(p, rankRepo.listRank[p].name, ArrayList(rankRepo.listName))

    fun onRenameDialog(p: Int, name: String) {
        rankRepo.set(p, name)

        val rankItem = rankRepo.listRank[p]

        val db = RoomDb.provideDb(context)
        db.daoRank().update(rankItem)
        db.close()

        callback.bindToolbar()
        callback.notifyItemChanged(p, rankItem)
    }

    fun onEditorClick(i: Int, name: String): Boolean {
        if (i != EditorInfo.IME_ACTION_DONE) return false

        if (!TextUtils.isEmpty(name) && !rankRepo.listName.contains(name)) {
            onClickAdd(simpleClick = true)
            return true
        }

        return false
    }

    fun onClickAdd(simpleClick: Boolean) {
        val name = callback.clearEnter()

        if (name.isEmpty()) return

        val p = if (simpleClick) rankRepo.size() else 0

        val rankItem = RankItem(if (simpleClick) p else -1, name)
        rankItem.id = iRoomRepo.insertRank(p, rankItem)

        rankRepo.add(p, rankItem)

        callback.scrollToItem(simpleClick, rankRepo.listRank)
    }

    override fun onResultTouchClear(dragFrom: Int, dragTo: Int) { // TODO: 03.02.2019 ошибка сортировки
        val db = RoomDb.provideDb(context)
        rankRepo.listRank.clear()
        rankRepo.listRank.addAll(db.daoRank().update(dragFrom, dragTo))
        db.daoNote().update(context)
        db.close()

        callback.notifyDataSetChanged(rankRepo.listRank)
    }

    override fun onResultTouchMove(from: Int, to: Int): Boolean {
        rankRepo.move(from, to)
        callback.notifyItemMoved(from, to, rankRepo.listRank)

        return true
    }

    fun onClickVisible(p: Int) {
        val rankItem = rankRepo.listRank[p]
        rankItem.isVisible = !rankItem.isVisible

        val db = RoomDb.provideDb(context)
        db.daoRank().update(rankItem)
        db.daoNote().update(context)
        db.close()

        callback.notifyVisible(p, rankItem)
    }

    fun onLongClickVisible(p: Int) {
        val listRank = rankRepo.listRank
        val startAnim = BooleanArray(listRank.size)

        val clickVisible = listRank[p].isVisible
        listRank.forEachIndexed { index, rankItem ->
            if (index == p || clickVisible != rankItem.isVisible) return@forEachIndexed

            rankItem.isVisible = !rankItem.isVisible
            startAnim[index] = true
        }

        callback.notifyVisible(startAnim, listRank)

        val db = RoomDb.provideDb(context)
        db.daoRank().updateRank(listRank)
        db.daoNote().update(context)
        db.close()
    }

    fun onClickCancel(p: Int) {
        val rankItem = rankRepo.listRank[p]

        val db = RoomDb.provideDb(context)
        db.daoRank().delete(rankItem.name)
        db.daoRank().update(p)
        db.daoNote().update(context)
        db.close()

        rankRepo.remove(p)

        callback.notifyItemRemoved(p, rankRepo.listRank)
    }

}