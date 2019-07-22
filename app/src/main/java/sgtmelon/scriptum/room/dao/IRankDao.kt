package sgtmelon.scriptum.room.dao

import androidx.room.*
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Interface for communicate [DbData.Rank.TABLE] with [RoomDb]
 *
 * @author SerjantArbuz
 */
@Dao
@TypeConverters(BoolConverter::class)
interface IRankDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(rankEntity: RankEntity): Long

    @Query(value = "DELETE FROM RANK_TABLE WHERE RK_NAME = :name")
    fun delete(name: String)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(item: RankEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(list: List<RankEntity>)

    @Query(value = "SELECT * FROM RANK_TABLE WHERE RK_NAME = :name")
    operator fun get(name: String): RankEntity?

    @Query(value = "SELECT * FROM RANK_TABLE ORDER BY RK_POSITION ASC")
    fun get(): MutableList<RankEntity>

    @Query(value = "SELECT * FROM RANK_TABLE WHERE RK_ID IN(:idList) ORDER BY RK_POSITION ASC")
    operator fun get(idList: List<Long>): List<RankEntity>

    @Query(value = "SELECT RK_ID FROM RANK_TABLE WHERE RK_VISIBLE = 1 ORDER BY RK_POSITION")
    fun getIdVisibleList(): List<Long>

    @Query(value = "SELECT COUNT(RK_ID) FROM RANK_TABLE")
    fun getCount(): Int

    @Query(value = "SELECT RK_NAME FROM RANK_TABLE ORDER BY RK_POSITION")
    fun getNameList(): List<String>

    @Query(value = "SELECT RK_ID FROM RANK_TABLE ORDER BY RK_POSITION")
    fun getIdList(): List<Long>

}