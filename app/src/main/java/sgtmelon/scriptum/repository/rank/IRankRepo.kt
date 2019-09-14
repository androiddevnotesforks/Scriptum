package sgtmelon.scriptum.repository.rank

import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Interface for communicate with [RankRepo]
 */
interface IRankRepo {

    /**
     * Обновление по категориям всех прикреплённых заметок в статус баре
     */
    suspend fun notifyBind()

    fun insert(rankEntity: RankEntity): Long

    fun get(): MutableList<RankEntity>

    fun delete(rankEntity: RankEntity)

    fun update(rankEntity: RankEntity)

    fun update(list: MutableList<RankEntity>)

}