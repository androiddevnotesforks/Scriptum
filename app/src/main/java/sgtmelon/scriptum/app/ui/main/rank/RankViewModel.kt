package sgtmelon.scriptum.app.ui.main.rank

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.RankRepo
import sgtmelon.scriptum.app.model.item.RankItem

/**
 * ViewModel для [RankFragment]
 */
class RankViewModel(application: Application) : AndroidViewModel(application) {

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

    fun onRenameDialog(p: Int, name: String) {
        rankRepo.set(p, name)

        val rankItem = rankRepo.listRank[p]

        val db = RoomDb.provideDb(context)
        db.daoRank().update(rankItem)
        db.close()

        callback.bindToolbar()
        callback.notifyItemChanged(p, rankItem)
    }

    fun onClickAdd(name: String, simpleClick: Boolean) = callback.addItem(when (simpleClick) {
        true -> onAddEnd(name)
        false -> onAddStart(name)
    }, simpleClick)

    private fun onAddEnd(name: String): MutableList<RankItem> {
        val p = rankRepo.size()
        val rankItem = RankItem(p, name)

        val db = RoomDb.provideDb(context)
        rankItem.id = db.daoRank().insert(rankItem)
        db.close()

        rankRepo.add(p, rankItem)

        return rankRepo.listRank
    }

    private fun onAddStart(name: String): MutableList<RankItem> {
        val p = 0
        val rankItem = RankItem(p - 1, name)

        val db = RoomDb.provideDb(context)
        rankItem.id = db.daoRank().insert(rankItem)
        db.daoRank().update(p)
        db.close()

        rankRepo.add(p, rankItem)

        return rankRepo.listRank
    }

    fun onUpdateDrag(dragFrom: Int, dragTo: Int): MutableList<RankItem> { // TODO: 03.02.2019 ошибка сортировки
        val db = RoomDb.provideDb(context)
        rankRepo.listRank.clear()
        rankRepo.listRank.addAll(db.daoRank().update(dragFrom, dragTo))
        db.daoNote().update(context)
        db.close()

        return rankRepo.listRank
    }

    fun onUpdateMove(positionFrom: Int, positionTo: Int): MutableList<RankItem> {
        rankRepo.move(positionFrom, positionTo)

        return rankRepo.listRank
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