package sgtmelon.scriptum.interactor.callback.main

import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.interactor.main.RankInteractor

import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Interface for communicate with [RankInteractor]
 */
interface IRankInteractor {

    suspend fun notifyBind(callback: BindControl.Bridge.Notify?)

    fun insert(name: String): RankEntity

    fun getList(): MutableList<RankEntity>

    fun delete(rankEntity: RankEntity)

    suspend fun update(rankEntity: RankEntity)

    fun update(list: List<RankEntity>)

}