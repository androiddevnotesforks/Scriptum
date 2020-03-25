package sgtmelon.scriptum.data.repository.room.callback

import sgtmelon.scriptum.data.repository.room.RankRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RankItem

/**
 * Interface for communicate with [RankRepo]
 */
interface IRankRepo {

    suspend fun getCount(): Int

    suspend fun getList(): MutableList<RankItem>

    suspend fun getIdVisibleList(): List<Long>


    suspend fun insert(name: String): Long

    suspend fun delete(rankItem: RankItem)

    suspend fun update(rankItem: RankItem)

    suspend fun update(rankList: List<RankItem>)

    suspend fun updatePosition(rankList: List<RankItem>, noteIdList: List<Long>)

    suspend fun updateConnection(noteItem: NoteItem)


    suspend fun getDialogItemArray(): Array<String>

    suspend fun getId(position: Int): Long

}