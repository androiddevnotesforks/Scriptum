package sgtmelon.scriptum.data.dataSource.database

import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity

interface RollDataSource {

    suspend fun insert(entity: RollEntity): Long?

    suspend fun update(id: Long, position: Int, text: String)

    suspend fun updateCheck(id: Long, isCheck: Boolean)

    suspend fun delete(noteId: Long)

    suspend fun delete(noteId: Long, excludeIdList: List<Long>)

    suspend fun getList(): List<RollEntity>

    suspend fun getList(noteId: Long): MutableList<RollEntity>

    suspend fun getList(noteIdList: List<Long>): List<RollEntity>

    suspend fun getPreviewList(noteId: Long): MutableList<RollEntity>

    suspend fun getPreviewHideList(noteId: Long): MutableList<RollEntity>
}