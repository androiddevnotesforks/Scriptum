package sgtmelon.scriptum.data.room.dao

import androidx.room.*
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.converter.type.BoolConverter
import sgtmelon.scriptum.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.domain.model.data.DbData

/**
 * Interface for communication [DbData.RollVisible.TABLE] with [RoomDb].
 */
@Dao
@TypeConverters(BoolConverter::class)
interface IRollVisibleDao {

    /**
     * TODO add -1 value convert to null
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: RollVisibleEntity): Long

    @Query(value = "UPDATE ROLL_VISIBLE_TABLE SET RL_VS_VALUE = :value WHERE RL_VS_NOTE_ID = :noteId")
    suspend fun update(noteId: Long, value: Boolean)

    @Query(value = "SELECT RL_VS_VALUE FROM ROLL_VISIBLE_TABLE WHERE RL_VS_NOTE_ID = :noteId")
    suspend fun get(noteId: Long): Boolean?

    @Query(value = "SELECT * FROM ROLL_VISIBLE_TABLE WHERE RL_VS_NOTE_ID IN (:noteIdList)")
    suspend fun get(noteIdList: List<Long>): List<RollVisibleEntity>

}