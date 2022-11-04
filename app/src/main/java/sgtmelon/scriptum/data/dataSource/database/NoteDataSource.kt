package sgtmelon.scriptum.data.dataSource.database

import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort

interface NoteDataSource {

    suspend fun insert(entity: NoteEntity): Long?

    suspend fun delete(entity: NoteEntity)

    suspend fun delete(list: List<NoteEntity>)

    suspend fun update(entity: NoteEntity)

    suspend fun update(list: List<NoteEntity>)


    @Deprecated("remove")
    suspend fun getRankVisibleCount(isBin: Boolean, rankIdList: List<Long>): Int

    suspend fun getBindCount(idList: List<Long>): Int


    suspend fun get(id: Long): NoteEntity?

    suspend fun getList(idList: List<Long>): List<NoteEntity>

    suspend fun getList(isBin: Boolean): List<NoteEntity>

    suspend fun getList(sort: Sort, isBin: Boolean): List<NoteEntity>
}