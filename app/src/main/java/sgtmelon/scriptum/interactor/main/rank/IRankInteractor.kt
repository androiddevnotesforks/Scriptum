package sgtmelon.scriptum.interactor.main.rank

import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Interface for communicate with [RankInteractor]
 */
interface IRankInteractor {

    suspend fun notifyBind()

    fun insert(name: String): RankEntity

    fun getList(): MutableList<RankEntity>

    fun delete(rankEntity: RankEntity)

    suspend fun update(rankEntity: RankEntity)

    fun update(list: List<RankEntity>)

}