package sgtmelon.scriptum.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import sgtmelon.scriptum.cleanup.data.room.Database
import sgtmelon.scriptum.cleanup.data.room.converter.type.BoolConverter
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData
import sgtmelon.scriptum.infrastructure.database.annotation.DaoDeprecated

/**
 * Interface for communication [DbData.RollVisible.TABLE] with [Database].
 */
// TODO remove from use deprecated staff (and use RollVisibleDataSource/RollVisibleDaoSafe)
@Dao
@TypeConverters(BoolConverter::class)
interface RollVisibleDao {

    @Deprecated(DaoDeprecated.INSERT_IGNORE_OR_KEY)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: RollVisibleEntity): Long

    @Query(value = "UPDATE ROLL_VISIBLE_TABLE SET RL_VS_VALUE = :value WHERE RL_VS_NOTE_ID = :noteId")
    suspend fun update(noteId: Long, value: Boolean)

    @Query(value = "SELECT * FROM ROLL_VISIBLE_TABLE ORDER BY RL_VS_NOTE_ID")
    suspend fun getList(): List<RollVisibleEntity>

    @Deprecated(DaoDeprecated.LIST_OVERFLOW)
    @Query(value = "SELECT * FROM ROLL_VISIBLE_TABLE WHERE RL_VS_NOTE_ID IN (:noteIdList)")
    suspend fun getList(noteIdList: List<Long>): List<RollVisibleEntity>

    @Query(value = "SELECT RL_VS_VALUE FROM ROLL_VISIBLE_TABLE WHERE RL_VS_NOTE_ID = :noteId")
    suspend fun getVisible(noteId: Long): Boolean?
}