package sgtmelon.scriptum.repository.rank

import sgtmelon.scriptum.model.RankModel
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Интерфейс для общения с [RankRepo]
 *
 * @author SerjantArbuz
 */
interface IRankRepo {

    /**
     * Обновление по категориям всех прикреплённых заметок в статус баре
     */
    suspend fun notifyBind()

    fun insertRank(p: Int, rankEntity: RankEntity): Long

    fun getRankModel(): RankModel

    fun deleteRank(name: String, p: Int)

    fun updateRank(dragFrom: Int, dragTo: Int): MutableList<RankEntity>

    fun updateRank(rankEntity: RankEntity)

    fun updateRank(rankList: List<RankEntity>)

}