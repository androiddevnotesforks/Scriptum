package sgtmelon.scriptum.infrastructure.database.dataSource

import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.infrastructure.database.dao.NoteDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.getBindCountSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.getListSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort

class NoteDataSourceImpl(private val dao: NoteDao) : NoteDataSource {

    override suspend fun insert(entity: NoteEntity): Long? = dao.insertSafe(entity)

    override suspend fun delete(entity: NoteEntity) = dao.delete(entity)

    override suspend fun delete(list: List<NoteEntity>) = dao.delete(list)

    override suspend fun update(entity: NoteEntity) = dao.update(entity)

    override suspend fun update(list: List<NoteEntity>) = dao.update(list)

    override suspend fun getBindCount(idList: List<Long>): Int = dao.getBindCountSafe(idList)

    override suspend fun get(id: Long): NoteEntity? = dao.get(id)

    override suspend fun getList(idList: List<Long>): List<NoteEntity> = dao.getListSafe(idList)

    override suspend fun getList(isBin: Boolean): List<NoteEntity> = dao.getList(isBin)

    override suspend fun getList(sort: Sort, isBin: Boolean): List<NoteEntity> {
        return when (sort) {
            Sort.CHANGE -> dao.getListByChange(isBin)
            Sort.CREATE -> dao.getListByCreate(isBin)
            Sort.RANK -> dao.getListByRank(isBin)
            Sort.COLOR -> dao.getListByColor(isBin)
        }
    }
}