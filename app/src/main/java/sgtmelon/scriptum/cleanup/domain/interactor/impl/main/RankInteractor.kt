package sgtmelon.scriptum.cleanup.domain.interactor.impl.main

import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankViewModel

/**
 * Interactor for [RankViewModel].
 */
class RankInteractor(private val rankRepo: RankRepo) : ParentInteractor(),
    IRankInteractor {

    override suspend fun getCount(): Int = rankRepo.getCount()

    override suspend fun updatePositions(list: List<RankItem>, noteIdList: List<Long>) {
        rankRepo.updatePositions(list, noteIdList)
    }
}