package sgtmelon.scriptum.data.room.dao

import androidx.room.*
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.annotation.DaoDeprecated
import sgtmelon.scriptum.data.room.converter.type.BoolConverter
import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.domain.model.data.DbData
import sgtmelon.scriptum.domain.model.item.NoteItem.Roll.Companion.PREVIEW_SIZE

/**
 * Interface for communication [DbData.Roll.TABLE] with [RoomDb].
 */
@Dao
@TypeConverters(BoolConverter::class)
interface IRollDao {

    @Deprecated(DaoDeprecated.INSERT)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(rollEntity: RollEntity): Long

    @Query(value = "UPDATE ROLL_TABLE SET RL_POSITION = :position, RL_TEXT = :text WHERE RL_ID = :id")
    suspend fun update(id: Long, position: Int, text: String)

    @Query(value = "UPDATE ROLL_TABLE SET RL_CHECK = :check WHERE RL_ID = :rollId")
    suspend fun update(rollId: Long, check: Boolean)

    @Query(value = "UPDATE ROLL_TABLE SET RL_CHECK = :check WHERE RL_NOTE_ID = :noteId")
    suspend fun updateAllCheck(noteId: Long, check: Boolean)

    /**
     * [saveList] - list of roll id's which don't need delete.
     */
    @Deprecated(DaoDeprecated.LIST)
    @Query(value = "DELETE FROM ROLL_TABLE WHERE RL_NOTE_ID = :noteId AND RL_ID NOT IN (:saveList)")
    suspend fun delete(noteId: Long, saveList: List<Long>)

    /**
     * [deleteList] - list of roll id's which need delete.
     */
    @Deprecated(DaoDeprecated.LIST)
    @Query(value = "DELETE FROM ROLL_TABLE WHERE RL_NOTE_ID = :noteId AND RL_ID IN (:deleteList)")
    suspend fun deleteByList(noteId: Long, deleteList: List<Long>)

    /**
     * Delete all items from note.
     */
    @Query(value = "DELETE FROM ROLL_TABLE WHERE RL_NOTE_ID = :noteId")
    suspend fun delete(noteId: Long)


    @Query(value = "SELECT * FROM ROLL_TABLE ORDER BY RL_NOTE_ID, RL_POSITION")
    suspend fun get(): List<RollEntity>

    @Query(value = "SELECT * FROM ROLL_TABLE WHERE RL_NOTE_ID = :noteId ORDER BY RL_POSITION")
    suspend fun get(noteId: Long): MutableList<RollEntity>

    @Deprecated(DaoDeprecated.LIST)
    @Query(value = "SELECT * FROM ROLL_TABLE WHERE RL_NOTE_ID IN (:noteIdList)")
    suspend fun get(noteIdList: List<Long>): List<RollEntity>

    /**
     * Get only first 4 items for preview.
     */
    @Query(value = """SELECT * FROM ROLL_TABLE
            WHERE RL_NOTE_ID = :noteId
            ORDER BY RL_POSITION
            LIMIT $PREVIEW_SIZE""")
    suspend fun getView(noteId: Long): MutableList<RollEntity>

    /**
     * Get only first 4 visible items (not hide) for preview.
     */
    @Query(value = """SELECT * FROM ROLL_TABLE
            WHERE RL_NOTE_ID = :noteId AND RL_CHECK = "0"
            ORDER BY RL_POSITION
            LIMIT $PREVIEW_SIZE""")
    suspend fun getViewHide(noteId: Long): MutableList<RollEntity>

}