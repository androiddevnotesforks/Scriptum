package sgtmelon.scriptum.interactor.main.rank

import android.content.Context
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Interactor for [RankViewModel]
 */
class RankInteractor(context: Context) : ParentInteractor(context), IRankInteractor {

    private val iRankRepo: IRankRepo = RankRepo(context)

    override suspend fun notifyBind() = iRankRepo.notifyBind()

    override fun insert(name: String) = RankEntity(name = name).apply {
        id = iRankRepo.insert(rankEntity = this)
    }

    override fun getList() = iRankRepo.getList()

    override fun delete(rankEntity: RankEntity) = iRankRepo.delete(rankEntity)

    override suspend fun update(rankEntity: RankEntity) = iRankRepo.update(rankEntity)

    override fun update(list: List<RankEntity>) = iRankRepo.update(list)

}