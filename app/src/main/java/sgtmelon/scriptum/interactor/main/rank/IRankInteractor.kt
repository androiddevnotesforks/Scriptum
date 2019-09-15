package sgtmelon.scriptum.interactor.main.rank

import sgtmelon.scriptum.control.notification.IBindBridge
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Interface for communicate with [RankInteractor]
 */
interface IRankInteractor {

    suspend fun notifyBind(callback: IBindBridge.Notify?)

    fun insert(name: String): RankEntity

    fun getList(): MutableList<RankEntity>

    fun delete(rankEntity: RankEntity)

    suspend fun update(rankEntity: RankEntity)

    fun update(list: List<RankEntity>)

}