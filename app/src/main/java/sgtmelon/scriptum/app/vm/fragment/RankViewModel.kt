package sgtmelon.scriptum.app.vm.fragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.RankRepo
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.view.fragment.RankFragment

/**
 * ViewModel для [RankFragment]
 */
class RankViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context = application.applicationContext

    var rankRepo: RankRepo = RankRepo(ArrayList(), ArrayList())
        private set

    fun loadData(): RankRepo {
        val db = RoomDb.provideDb(context)
        rankRepo = db.daoRank().get()
        db.close()

        return rankRepo
    }

    fun onDialogRename(p: Int, name: String): RankItem {
        rankRepo.set(p, name)

        val rankItem = rankRepo.listRank[p]

        val db = RoomDb.provideDb(context)
        db.daoRank().update(rankItem)
        db.close()

        return rankItem
    }

    fun onAddEnd(name: String): MutableList<RankItem> {
        val p = rankRepo.size()
        val rankItem = RankItem(p, name)

        val db = RoomDb.provideDb(context)
        rankItem.id = db.daoRank().insert(rankItem)
        db.close()

        rankRepo.add(p, rankItem)

        return rankRepo.listRank
    }

    fun onAddStart(name: String): MutableList<RankItem> {
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

    fun onUpdateVisible(p: Int): RankItem {
        val rankItem = rankRepo.listRank[p]
        rankItem.isVisible = !rankItem.isVisible

        val db = RoomDb.provideDb(context)
        db.daoRank().update(rankItem)
        db.daoNote().update(context)
        db.close()

        return rankItem
    }

    fun onUpdateVisible(listRank: MutableList<RankItem>) {
        val db = RoomDb.provideDb(context)
        db.daoRank().updateRank(listRank)
        db.daoNote().update(context)
        db.close()
    }

    fun onCancel(p: Int): MutableList<RankItem> {
        val rankItem = rankRepo.listRank[p]

        val db = RoomDb.provideDb(context)
        db.daoRank().delete(rankItem.name)
        db.daoRank().update(p)
        db.daoNote().update(context)
        db.close()

        rankRepo.remove(p)

        return rankRepo.listRank
    }

}