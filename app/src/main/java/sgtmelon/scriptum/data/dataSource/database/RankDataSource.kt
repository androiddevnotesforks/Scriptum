package sgtmelon.scriptum.data.dataSource.database

import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem

interface RankDataSource {

    suspend fun insert(entity: RankEntity): Long?

    suspend fun delete(item: RankItem)

    suspend fun update(entity: RankEntity)

    suspend fun update(list: List<RankEntity>)

    suspend fun getCount(): Int

    suspend fun get(id: Long): RankEntity?

    suspend fun getList(): List<RankEntity>

    suspend fun getIdVisibleList(): List<Long>

    @Deprecated("remove")
    suspend fun getIdList(): List<Long>

    suspend fun getNameList(): List<String>

    suspend fun getId(position: Int): Long?
}