package sgtmelon.scriptum.interactor.callback.main

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.main.RankInteractor

import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Interface for communication [RankViewModel] with [RankInteractor]
 */
interface IRankInteractor : IParentInteractor {

    fun insert(name: String): RankEntity

    fun getList(): MutableList<RankEntity>

    fun delete(rankEntity: RankEntity)

    suspend fun update(rankEntity: RankEntity)

    fun update(list: List<RankEntity>)

}