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

    fun delete(rankEntity: RankItem)

    suspend fun update(rankEntity: RankItem)

    fun update(list: List<RankItem>)

}