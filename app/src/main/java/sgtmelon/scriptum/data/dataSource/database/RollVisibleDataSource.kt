package sgtmelon.scriptum.data.dataSource.database

import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity

interface RollVisibleDataSource {

    suspend fun insert(entity: RollVisibleEntity): Long?

    suspend fun update(noteId: Long, value: Boolean)

    suspend fun getList(): List<RollVisibleEntity>

    suspend fun getList(noteIdList: List<Long>): List<RollVisibleEntity>

    suspend fun getVisible(noteId: Long): Boolean?
}