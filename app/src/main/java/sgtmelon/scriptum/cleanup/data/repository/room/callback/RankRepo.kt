package sgtmelon.scriptum.cleanup.data.repository.room.callback

import sgtmelon.scriptum.cleanup.data.repository.room.RankRepoImpl
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem

/**
 * Interface for communicate with [RankRepoImpl]
 */
interface RankRepo {

    suspend fun getList(): List<RankItem>


    suspend fun insert(name: String): RankItem?

    suspend fun insert(item: RankItem)

    suspend fun delete(item: RankItem)

    suspend fun update(item: RankItem)

    suspend fun updatePositions(list: List<RankItem>, noteIdList: List<Long>)

    suspend fun updateConnection(item: NoteItem)


    suspend fun getNameList(): List<String>

    suspend fun getId(position: Int): Long?
}