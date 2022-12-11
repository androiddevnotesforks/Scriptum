package sgtmelon.scriptum.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.infrastructure.converter.types.BoolConverter
import sgtmelon.scriptum.infrastructure.database.Database
import sgtmelon.scriptum.infrastructure.database.DbData
import sgtmelon.scriptum.infrastructure.database.model.DaoDeprecated

/**
 * Communication between [DbData.Rank.TABLE] and [Database].
 */
@Dao
@TypeConverters(BoolConverter::class)
interface RankDao {

    // TODO use rankItem not entity (check, it's actual for now?)

    @Deprecated(DaoDeprecated.INSERT_IGNORE)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: RankEntity): Long

    @Query(value = "DELETE FROM RANK_TABLE WHERE RK_NAME = :name")
    suspend fun delete(name: String)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entity: RankEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(list: List<RankEntity>)

    @Query(value = "SELECT * FROM RANK_TABLE WHERE RK_ID == :id")
    suspend fun get(id: Long): RankEntity?

    @Query(value = "SELECT * FROM RANK_TABLE ORDER BY RK_POSITION ASC")
    suspend fun getList(): List<RankEntity>

    @Query(value = "SELECT RK_ID FROM RANK_TABLE WHERE RK_VISIBLE = 1 ORDER BY RK_POSITION")
    suspend fun getIdVisibleList(): List<Long>

    @Query(value = "SELECT RK_NAME FROM RANK_TABLE ORDER BY RK_POSITION")
    suspend fun getNameList(): List<String>

    @Query(value = "SELECT RK_ID FROM RANK_TABLE WHERE RK_POSITION = :position")
    suspend fun getId(position: Int): Long?
}