package sgtmelon.scriptum.interactor.main

import android.content.Context

import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Interactor for [RankViewModel]
 */
class RankInteractor(context: Context) : ParentInteractor(context), IRankInteractor {

    private val iRankRepo: IRankRepo = RankRepo(context)


    override fun insert(name: String): RankItem = RankEntity(name = name).apply {
        id = iRankRepo.insert(rankEntity = this)
    }

    override fun getList(): MutableList<RankItem> {
        iRankRepo.getList()
        return arrayListOf()
    }

    override fun delete(rankEntity: RankItem) = iRankRepo.delete(rankEntity)

    override suspend fun update(rankEntity: RankItem) = iRankRepo.update(rankEntity)

    override fun update(list: List<RankItem>) = iRankRepo.update(list)

}