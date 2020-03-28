package sgtmelon.scriptum.domain.interactor.main

import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.interactor.ParentInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.presentation.screen.vm.impl.main.RankViewModel

/**
 * Interactor for [RankViewModel].
 */
class RankInteractor(private val rankRepo: IRankRepo) : ParentInteractor(), IRankInteractor {

    override suspend fun getCount() = rankRepo.getCount()

    override suspend fun getList() = rankRepo.getList()

    override suspend fun insert(name: String) = RankItem(rankRepo.insert(name), name = name)

    override suspend fun delete(item: RankItem) = rankRepo.delete(item)

    override suspend fun update(item: RankItem) = rankRepo.update(item)

    override suspend fun update(list: List<RankItem>) = rankRepo.update(list)

    override suspend fun updatePosition(list: List<RankItem>, noteIdList: List<Long>) {
        rankRepo.updatePosition(list, noteIdList)
    }

}