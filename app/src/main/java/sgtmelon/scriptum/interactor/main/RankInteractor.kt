package sgtmelon.scriptum.interactor.main

import android.content.Context

import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Interactor for [RankViewModel]
 */
class RankInteractor(context: Context) : ParentInteractor(context), IRankInteractor {

    private val iRankRepo: IRankRepo = RankRepo(context)


    override fun insert(name: String) = iRankRepo.insert(name)

    override fun getList() = iRankRepo.getList()

    override fun delete(item: RankItem) = iRankRepo.delete(item)

    override suspend fun update(item: RankItem) = iRankRepo.update(item)

    override fun updatePosition(list: List<RankItem>) = iRankRepo.updatePosition(list)

}