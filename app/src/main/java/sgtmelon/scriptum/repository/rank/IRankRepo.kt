package sgtmelon.scriptum.repository.rank

import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Interface for communicate with [RankRepo]
 */
interface IRankRepo {

    fun insert(name: String): Long

    fun getList(): MutableList<RankEntity>

    fun delete(rankEntity: RankEntity)

    fun update(rankEntity: RankEntity)

    fun updatePosition(rankList: List<RankEntity>, noteIdList: List<Long>)

    fun updateConnection(noteModel: NoteModel)

}