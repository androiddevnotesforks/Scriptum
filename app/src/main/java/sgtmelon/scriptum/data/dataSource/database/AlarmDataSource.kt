package sgtmelon.scriptum.data.dataSource.database

import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

interface AlarmDataSource {

    suspend fun insert(entity: AlarmEntity): Long?

    suspend fun delete(noteId: Long)

    suspend fun update(entity: AlarmEntity)

    suspend fun get(noteId: Long): AlarmEntity?

    suspend fun getList(): List<AlarmEntity>

    suspend fun getList(noteIdList: List<Long>): List<AlarmEntity>

    suspend fun getItemList(): List<NotificationItem>

    suspend fun getDateList(): List<String>

    suspend fun getCount(): Int

    suspend fun getCount(noteIdList: List<Long>): Int
}