package sgtmelon.scriptum.cleanup.domain.interactor.callback.main

import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.RankInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankViewModel

/**
 * Interface for communication [RankViewModel] with [RankInteractor].
 */
interface IRankInteractor : IParentInteractor {

    suspend fun getCount(): Int

    @Deprecated("Move it inside use case")
    suspend fun updatePositions(list: List<RankItem>, noteIdList: List<Long>)

}