package sgtmelon.scriptum.repository.rank

import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Interface for communicate with [RankRepo]
 */
interface IRankRepo {

    fun isEmpty(): Boolean

    fun getList(): MutableList<RankEntity>

    fun getIdVisibleList(): List<Long>


    fun insert(name: String): Long

    fun delete(rankEntity: RankEntity)

    fun update(rankEntity: RankEntity)

    fun updatePosition(rankList: List<RankEntity>, noteIdList: List<Long>)

    fun updateConnection(noteModel: NoteModel)


    fun getDialogItemArray(): Array<String>

    fun getId(check: Int): Long

}