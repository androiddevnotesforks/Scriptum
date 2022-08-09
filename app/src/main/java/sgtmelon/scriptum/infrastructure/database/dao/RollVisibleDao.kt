package sgtmelon.scriptum.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import sgtmelon.scriptum.cleanup.data.room.RoomDb
import sgtmelon.scriptum.cleanup.data.room.converter.type.BoolConverter
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData
import sgtmelon.scriptum.infrastructure.database.annotation.DaoDeprecated

/**
 * Interface for communication [DbData.RollVisible.TABLE] with [RoomDb].
 */
@Dao
@TypeConverters(BoolConverter::class)
interface RollVisibleDao {

    @Deprecated(DaoDeprecated.INSERT_IGNORE)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: RollVisibleEntity): Long

    @Query(value = "UPDATE ROLL_VISIBLE_TABLE SET RL_VS_VALUE = :value WHERE RL_VS_NOTE_ID = :noteId")
    suspend fun update(noteId: Long, value: Boolean)

    @Query(value = "SELECT * FROM ROLL_VISIBLE_TABLE ORDER BY RL_VS_NOTE_ID")
    suspend fun get(): List<RollVisibleEntity>

    @Query(value = "SELECT RL_VS_VALUE FROM ROLL_VISIBLE_TABLE WHERE RL_VS_NOTE_ID = :noteId")
    suspend fun get(noteId: Long): Boolean?

    @Deprecated(DaoDeprecated.LIST_OVERFLOW)
    @Query(value = "SELECT * FROM ROLL_VISIBLE_TABLE WHERE RL_VS_NOTE_ID IN (:noteIdList)")
    suspend fun get(noteIdList: List<Long>): List<RollVisibleEntity>

}