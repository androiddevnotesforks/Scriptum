package sgtmelon.scriptum.interactor.callback.main

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.main.RankInteractor
import sgtmelon.scriptum.model.item.RankItem

import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Interface for communication [RankViewModel] with [RankInteractor]
 */
interface IRankInteractor : IParentInteractor {

    fun insert(name: String): RankItem

    fun getList(): MutableList<RankItem>

    fun delete(item: RankItem)

    suspend fun update(item: RankItem)

    fun updatePosition(list: List<RankItem>)

}