package sgtmelon.scriptum.repository.rank

import sgtmelon.scriptum.model.RankModel
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Interface for communication with [RankRepo]
 *
 * @author SerjantArbuz
 */
interface IRankRepo {

    /**
     * Обновление по категориям всех прикреплённых заметок в статус баре
     */
    suspend fun notifyBind()

    fun insert(p: Int, rankEntity: RankEntity): Long

    fun getRankModel(): RankModel

    fun delete(name: String, p: Int)

    fun update(rankEntity: RankEntity)

    fun update(list: MutableList<RankEntity>)

}