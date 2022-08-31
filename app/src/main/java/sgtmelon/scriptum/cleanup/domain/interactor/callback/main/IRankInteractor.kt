package sgtmelon.scriptum.cleanup.domain.interactor.callback.main

import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.RankInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IRankViewModel

/**
 * Interface for communication [IRankViewModel] with [RankInteractor].
 */
interface IRankInteractor : IParentInteractor {

    suspend fun getCount(): Int

    suspend fun getList(): MutableList<RankItem>


    suspend fun insert(name: String): RankItem?

    suspend fun insert(item: RankItem)

    suspend fun delete(item: RankItem)

    suspend fun update(item: RankItem)

    suspend fun update(list: List<RankItem>)

    suspend fun updatePositions(list: List<RankItem>, noteIdList: List<Long>)

}