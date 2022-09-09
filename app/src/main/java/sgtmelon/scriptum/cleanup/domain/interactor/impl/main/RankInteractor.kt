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

    override suspend fun updatePositions(list: List<RankItem>, noteIdList: List<Long>) {
        rankRepo.updatePositions(list, noteIdList)
    }
}