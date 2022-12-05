package sgtmelon.scriptum.infrastructure.database.dataSource

import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.data.dataSource.database.RollDataSource
import sgtmelon.scriptum.infrastructure.database.dao.RollDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.deleteSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.getListSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe

class RollDataSourceImpl(private val dao: RollDao) : RollDataSource {

    override suspend fun insert(entity: RollEntity): Long? = dao.insertSafe(entity)

    override suspend fun update(id: Long, position: Int, text: String) {
        dao.update(id, position, text)
    }

    override suspend fun updateCheck(id: Long, isCheck: Boolean) = dao.updateCheck(id, isCheck)

    override suspend fun delete(noteId: Long) = dao.delete(noteId)

    override suspend fun delete(noteId: Long, excludeIdList: List<Long>) {
        dao.deleteSafe(noteId, excludeIdList)
    }

    override suspend fun getList(): List<RollEntity> = dao.getList()

    override suspend fun getList(noteId: Long): MutableList<RollEntity> = dao.getList(noteId)

    override suspend fun getList(noteIdList: List<Long>): List<RollEntity> {
        return dao.getListSafe(noteIdList)
    }

    override suspend fun getPreviewList(noteId: Long): MutableList<RollEntity> {
        return dao.getPreviewList(noteId)
    }

    override suspend fun getPreviewHideList(noteId: Long): MutableList<RollEntity> {
        return dao.getPreviewHideList(noteId)
    }
}