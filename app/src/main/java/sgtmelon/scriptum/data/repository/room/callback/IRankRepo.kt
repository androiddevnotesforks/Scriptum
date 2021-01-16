package sgtmelon.scriptum.data.repository.room.callback

import sgtmelon.scriptum.data.repository.room.RankRepo
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RankItem

/**
 * Interface for communicate with [RankRepo]
 */
interface IRankRepo {

    suspend fun getCount(): Int

    suspend fun getList(): MutableList<RankItem>

    suspend fun getBind(idList: List<Long>): Boolean

    suspend fun getIdVisibleList(): List<Long>


    suspend fun insert(name: String): Long?

    suspend fun insert(item: RankItem)

    suspend fun delete(item: RankItem)

    suspend fun update(item: RankItem)

    suspend fun update(list: List<RankItem>)

    suspend fun updatePosition(list: List<RankItem>, idList: List<Long>)

    suspend fun updateConnection(item: NoteItem)


    suspend fun getDialogItemArray(emptyName: String): Array<String>

    suspend fun getId(position: Int): Long


    suspend fun getRankBackup(): List<RankEntity>

}