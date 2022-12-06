package sgtmelon.scriptum.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem.Roll.Companion.PREVIEW_SIZE
import sgtmelon.scriptum.infrastructure.converter.types.BoolConverter
import sgtmelon.scriptum.infrastructure.database.Database
import sgtmelon.scriptum.infrastructure.database.model.DaoDeprecated

/**
 * Communication between [DbData.Roll.TABLE] and [Database].
 */
@Dao
@TypeConverters(BoolConverter::class)
interface RollDao {

    @Deprecated(DaoDeprecated.INSERT_IGNORE_OR_KEY)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: RollEntity): Long

    @Query(
        value = """UPDATE ROLL_TABLE 
            SET RL_POSITION = :position, RL_TEXT = :text 
            WHERE RL_ID = :id"""
    )
    suspend fun update(id: Long, position: Int, text: String)

    @Query(value = "UPDATE ROLL_TABLE SET RL_CHECK = :isCheck WHERE RL_ID = :id")
    suspend fun updateCheck(id: Long, isCheck: Boolean)

    /**
     * Delete all items from note.
     */
    @Query(value = "DELETE FROM ROLL_TABLE WHERE RL_NOTE_ID = :noteId")
    suspend fun delete(noteId: Long)

    /**
     * [excludeIdList] - list of roll id's which don't need delete.
     */
    @Deprecated(DaoDeprecated.LIST_OVERFLOW)
    @Query(
        value = """DELETE FROM ROLL_TABLE  
           WHERE RL_NOTE_ID = :noteId AND RL_ID NOT IN (:excludeIdList)"""
    )
    suspend fun delete(noteId: Long, excludeIdList: List<Long>)

    /**
     * [idList] - list of roll id's which need delete.
     */
    @Deprecated(DaoDeprecated.LIST_OVERFLOW)
    @Query(value = "DELETE FROM ROLL_TABLE WHERE RL_ID IN (:idList)")
    suspend fun delete(idList: List<Long>)

    @Query(value = "SELECT * FROM ROLL_TABLE ORDER BY RL_NOTE_ID, RL_POSITION")
    suspend fun getList(): List<RollEntity>

    // TODO make list not mutable?
    @Query(value = "SELECT * FROM ROLL_TABLE WHERE RL_NOTE_ID = :noteId ORDER BY RL_POSITION")
    suspend fun getList(noteId: Long): MutableList<RollEntity>

    @Query(value = "SELECT RL_ID FROM ROLL_TABLE WHERE RL_NOTE_ID = :noteId")
    suspend fun getIdList(noteId: Long): List<Long>

    @Deprecated(DaoDeprecated.LIST_OVERFLOW)
    @Query(value = "SELECT * FROM ROLL_TABLE WHERE RL_NOTE_ID IN (:noteIdList)")
    suspend fun getList(noteIdList: List<Long>): List<RollEntity>

    // TODO make list not mutable?
    /**
     * Get only first 4 items for preview.
     */
    @Query(
        value = """SELECT * FROM ROLL_TABLE
            WHERE RL_NOTE_ID = :noteId
            ORDER BY RL_POSITION
            LIMIT $PREVIEW_SIZE"""
    )
    suspend fun getPreviewList(noteId: Long): MutableList<RollEntity>

    // TODO make list not mutable?
    /**
     * Get only first 4 visible items (not hide) for preview.
     */
    @Query(
        value = """SELECT * FROM ROLL_TABLE
            WHERE RL_NOTE_ID = :noteId AND RL_CHECK = "0"
            ORDER BY RL_POSITION
            LIMIT $PREVIEW_SIZE"""
    )
    suspend fun getPreviewHideList(noteId: Long): MutableList<RollEntity>
}