package sgtmelon.scriptum.repository.rank

import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.item.RankItem

/**
 * Interface for communicate with [RankRepo]
 */
interface IRankRepo {

    fun insert(name: String): RankItem

    fun getList(): MutableList<RankItem>

    fun delete(item: RankItem)

    fun update(item: RankItem)

    fun updatePosition(list: List<RankItem>)

    fun updateConnection(noteModel: NoteModel)

}