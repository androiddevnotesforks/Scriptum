package sgtmelon.scriptum.infrastructure.database.dataSource

import sgtmelon.scriptum.cleanup.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.getCountSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.getSafe

class AlarmDataSourceImpl(private val dao: AlarmDao) : AlarmDataSource {

    override suspend fun insert(entity: AlarmEntity): Long = dao.insert(entity)

    override suspend fun delete(noteId: Long) = dao.delete(noteId)

    override suspend fun update(entity: AlarmEntity) = dao.update(entity)

    override suspend fun get(noteId: Long): AlarmEntity? = dao.get(noteId)

    override suspend fun get(): List<AlarmEntity> = dao.get()

    override suspend fun get(noteIdList: List<Long>): List<AlarmEntity> = dao.getSafe(noteIdList)

    override suspend fun getItem(noteId: Long): NotificationItem? = dao.getItem(noteId)

    override suspend fun getItemList(): List<NotificationItem> = dao.getItemList()

    override suspend fun getCount(): Int = dao.getCount()

    override suspend fun getCount(noteIdList: List<Long>): Int = dao.getCountSafe(noteIdList)

}