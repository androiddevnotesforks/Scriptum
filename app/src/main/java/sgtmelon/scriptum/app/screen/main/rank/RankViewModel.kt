package sgtmelon.scriptum.app.screen.main.rank

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.control.touch.RankTouchControl
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.RankRepo
import sgtmelon.scriptum.app.model.item.RankItem

/**
 * ViewModel для [RankFragment]
 */
class RankViewModel(application: Application) : AndroidViewModel(application),
        RankTouchControl.Result {

    private val context: Context = application.applicationContext

    lateinit var callback: RankCallback

    var rankRepo: RankRepo = RankRepo(ArrayList(), ArrayList())
        private set

    fun onLoadData() {
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

    fun onClickAdd(simpleClick: Boolean) = callback.addItem(simpleClick, when (simpleClick) {
        true -> onAddEnd()
        false -> onAddStart()
    })

    private fun onAddEnd(): MutableList<RankItem> {
        val name = callback.clearEnter()

        val p = rankRepo.size()
        val rankItem = RankItem(p, name)

        val db = RoomDb.provideDb(context)
        rankItem.id = db.daoRank().insert(rankItem)
        db.close()

        rankRepo.add(p, rankItem)

        return rankRepo.listRank
    }

    private fun onAddStart(): MutableList<RankItem> {
        val name = callback.clearEnter()

        val p = 0
        val rankItem = RankItem(p - 1, name)

        val db = RoomDb.provideDb(context)
        rankItem.id = db.daoRank().insert(rankItem)
        db.daoRank().update(p)
        db.close()

        rankRepo.add(p, rankItem)

        return rankRepo.listRank
    }

    override fun onTouchClear(dragFrom: Int, dragTo: Int) { // TODO: 03.02.2019 ошибка сортировки
        val db = RoomDb.provideDb(context)
        rankRepo.listRank.clear()
        rankRepo.listRank.addAll(db.daoRank().update(dragFrom, dragTo))
        db.daoNote().update(context)
        db.close()

        callback.notifyDataSetChanged(rankRepo.listRank)
    }

    override fun onTouchMove(from: Int, to: Int) {
        rankRepo.move(from, to)
        callback.notifyItemMoved(from, to, rankRepo.listRank)
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