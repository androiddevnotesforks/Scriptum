package sgtmelon.scriptum.interactor.main

import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.repository.room.callback.IRankRepo
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Interactor for [RankViewModel].
 */
class RankInteractor(private val iRankRepo: IRankRepo) : ParentInteractor(), IRankInteractor {

    override suspend fun getCount() = iRankRepo.getCount()

    override suspend fun getList() = iRankRepo.getList()

    override suspend fun insert(name: String) = RankItem(iRankRepo.insert(name), name = name)

    override suspend fun delete(item: RankItem) = iRankRepo.delete(item)

    override suspend fun update(item: RankItem) = iRankRepo.update(item)

    override suspend fun update(list: List<RankItem>) = iRankRepo.update(list)

    override suspend fun updatePosition(list: List<RankItem>, noteIdList: List<Long>) {
        iRankRepo.updatePosition(list, noteIdList)
    }

}