package sgtmelon.scriptum.infrastructure.database.dataSource

import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.getCountSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.getListSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe

class AlarmDataSourceImpl(private val dao: AlarmDao) : AlarmDataSource {

    override suspend fun insert(entity: AlarmEntity): Long? = dao.insertSafe(entity)

    override suspend fun delete(noteId: Long) = dao.delete(noteId)

    override suspend fun update(entity: AlarmEntity) = dao.update(entity)

    override suspend fun get(noteId: Long): AlarmEntity? = dao.get(noteId)

    override suspend fun getList(): List<AlarmEntity> = dao.getList()

    override suspend fun getList(noteIdList: List<Long>): List<AlarmEntity> {
        return dao.getListSafe(noteIdList)
    }

    override suspend fun getItemList(): List<NotificationItem> = dao.getItemList()

    override suspend fun getDateList(): List<String> = dao.getDateList()

    override suspend fun getCount(): Int = dao.getCount()

    override suspend fun getCount(noteIdList: List<Long>): Int = dao.getCountSafe(noteIdList)
}