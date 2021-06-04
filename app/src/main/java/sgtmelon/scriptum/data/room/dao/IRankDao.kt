package sgtmelon.scriptum.data.room.dao

import androidx.room.*
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.annotation.DaoDeprecated
import sgtmelon.scriptum.data.room.converter.type.BoolConverter
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.domain.model.data.DbData

/**
 * Interface for communication [DbData.Rank.TABLE] with [RoomDb].
 */
@Dao
@TypeConverters(BoolConverter::class)
interface IRankDao {

    // TODO use rankItem not entity

    @Deprecated(DaoDeprecated.INSERT)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: RankEntity): Long

    @Query(value = "DELETE FROM RANK_TABLE WHERE RK_NAME = :name")
    suspend fun delete(name: String)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entity: RankEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(list: List<RankEntity>)



    @Query(value = "SELECT COUNT(RK_ID) FROM RANK_TABLE")
    suspend fun getCount(): Int

    @Query(value = "SELECT * FROM RANK_TABLE WHERE RK_ID == :id")
    suspend fun get(id: Long): RankEntity?

    @Query(value = "SELECT * FROM RANK_TABLE ORDER BY RK_POSITION ASC")
    suspend fun get(): List<RankEntity>

    @Query(value = "SELECT RK_ID FROM RANK_TABLE WHERE RK_VISIBLE = 1 ORDER BY RK_POSITION")
    suspend fun getIdVisibleList(): List<Long>

    @Query(value = "SELECT RK_ID FROM RANK_TABLE ORDER BY RK_POSITION")
    suspend fun getIdList(): List<Long>

    @Query(value = "SELECT RK_NAME FROM RANK_TABLE ORDER BY RK_POSITION")
    suspend fun getNameList(): List<String>

    @Query(value = "SELECT RK_ID FROM RANK_TABLE WHERE RK_POSITION = :position")
    suspend fun getId(position: Int): Long?

}