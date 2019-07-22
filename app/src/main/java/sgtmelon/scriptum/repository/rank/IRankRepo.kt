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

    fun insert(rankEntity: RankEntity): Long

    fun getRankModel(): RankModel

    fun delete(name: String)

    fun update(rankEntity: RankEntity)

    fun update(list: MutableList<RankEntity>)

}