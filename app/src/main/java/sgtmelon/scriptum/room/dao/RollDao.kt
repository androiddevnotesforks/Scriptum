package sgtmelon.scriptum.room.dao

import androidx.room.*
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Interface for communicate [DbData.Roll.TABLE] with [RoomDb]
 *
 * @author SerjantArbuz
 */
@Dao
@TypeConverters(BoolConverter::class)
interface RollDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(rollEntity: RollEntity): Long

    @Query(value = "UPDATE ROLL_TABLE SET RL_POSITION = :position, RL_TEXT = :text WHERE RL_ID = :id")
    fun update(id: Long, position: Int, text: String)

    @Query(value = "UPDATE ROLL_TABLE SET RL_CHECK = :check WHERE RL_ID = :rollId")
    fun update(rollId: Long, check: Boolean)

    @Query(value = "UPDATE ROLL_TABLE SET RL_CHECK = :check WHERE RL_NOTE_ID = :noteId")
    fun updateAllCheck(noteId: Long, check: Boolean)

    /**
     * Удаление пунктов при сохранении после свайпа
     *
     * @param noteId - Id заметки
     * @param idSaveList - Id, которые остались в заметке
     */
    @Query(value = "DELETE FROM ROLL_TABLE WHERE RL_NOTE_ID = :noteId AND RL_ID NOT IN (:idSaveList)")
    fun delete(noteId: Long, idSaveList: List<Long>)

    /**
     * Delete all items from note
     */
    @Query(value = "DELETE FROM ROLL_TABLE WHERE RL_NOTE_ID = :noteId")
    fun delete(noteId: Long)

    @Query(value = "SELECT * FROM ROLL_TABLE WHERE RL_NOTE_ID = :noteId ORDER BY RL_POSITION")
    operator fun get(noteId: Long): MutableList<RollEntity>

    /**
     * Get only first 4 items for preview
     */
    @Query(value = """SELECT * FROM ROLL_TABLE
            WHERE RL_NOTE_ID = :noteId AND RL_POSITION BETWEEN 0 AND 3
            ORDER BY RL_POSITION ASC""")
    fun getView(noteId: Long): MutableList<RollEntity>

}