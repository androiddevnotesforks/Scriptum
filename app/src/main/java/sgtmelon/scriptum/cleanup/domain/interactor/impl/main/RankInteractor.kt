package sgtmelon.scriptum.cleanup.domain.interactor.impl.main

import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IRankViewModel

/**
 * Interactor for [IRankViewModel].
 */
class RankInteractor(private val rankRepo: RankRepo) : ParentInteractor(),
    IRankInteractor {

    override suspend fun getCount(): Int = rankRepo.getCount()

    override suspend fun getList(): MutableList<RankItem> = rankRepo.getList()


    override suspend fun insert(name: String): RankItem? = rankRepo.insert(name)

    override suspend fun insert(item: RankItem) = rankRepo.insert(item)

    override suspend fun delete(item: RankItem) = rankRepo.delete(item)

    override suspend fun update(item: RankItem) = rankRepo.update(item)

    override suspend fun update(list: List<RankItem>) = rankRepo.update(list)

    override suspend fun updatePositions(list: List<RankItem>, noteIdList: List<Long>) {
        rankRepo.updatePositions(list, noteIdList)
    }
}