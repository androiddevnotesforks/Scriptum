package sgtmelon.scriptum.interactor.callback.main

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.main.RankInteractor
import sgtmelon.scriptum.model.item.RankItem

import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Interface for communication [RankViewModel] with [RankInteractor]
 */
interface IRankInteractor : IParentInteractor {

    suspend fun insert(name: String): RankItem

    suspend fun getList(): MutableList<RankItem>

    suspend fun delete(item: RankItem)

    suspend fun update(item: RankItem)

    suspend fun update(list: List<RankItem>)

    suspend fun updatePosition(list: List<RankItem>, noteIdList: List<Long>)

}