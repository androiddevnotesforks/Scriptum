package sgtmelon.scriptum.app.vm.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.RankRepo
import sgtmelon.scriptum.app.view.fragment.RankFragment

/**
 * ViewModel для [RankFragment]
 */
class RankViewModel(application: Application) : AndroidViewModel(application) {

    var rankRepo: RankRepo = RankRepo(ArrayList(), ArrayList())
        private set

    fun loadData(): RankRepo {
        val db = RoomDb.provideDb(getApplication<Application>().applicationContext)
        rankRepo = db.daoRank().get()
        db.close()

        return rankRepo
    }

}