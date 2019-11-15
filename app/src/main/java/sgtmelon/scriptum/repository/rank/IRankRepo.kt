package sgtmelon.scriptum.repository.rank

import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RankItem

/**
 * Interface for communicate with [RankRepo]
 */
interface IRankRepo {

    fun isEmpty(): Boolean

    fun getList(): MutableList<RankItem>

    fun getIdVisibleList(): List<Long>


    fun insert(name: String): Long

    fun delete(rankItem: RankItem)

    fun update(rankItem: RankItem)

    fun updatePosition(rankList: List<RankItem>)

    fun updateConnection(noteItem: NoteItem)


    fun getDialogItemArray(): Array<String>

    fun getId(check: Int): Long

}