package sgtmelon.scriptum.infrastructure.database.dataSource

import sgtmelon.scriptum.cleanup.data.dataSource.database.RollVisibleDataSource
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.infrastructure.database.dao.RollVisibleDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.getListSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe

class RollVisibleDataSourceImpl(private val dao: RollVisibleDao) : RollVisibleDataSource {

    override suspend fun insert(entity: RollVisibleEntity): Long? = dao.insertSafe(entity)

    override suspend fun update(noteId: Long, value: Boolean) = dao.update(noteId, value)

    override suspend fun getList(): List<RollVisibleEntity> = dao.getList()

    override suspend fun getList(noteIdList: List<Long>): List<RollVisibleEntity> {
        return dao.getListSafe(noteIdList)
    }

    override suspend fun getVisible(noteId: Long): Boolean? = dao.getVisible(noteId)
}