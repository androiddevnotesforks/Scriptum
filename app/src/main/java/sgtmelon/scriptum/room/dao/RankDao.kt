package sgtmelon.scriptum.room.dao

import androidx.room.*
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Класс для общения Dao категорий [RoomDb]
 *
 * @author SerjantArbuz
 */
@Dao
@TypeConverters(BoolConverter::class)
interface RankDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(rankEntity: RankEntity): Long

    @Delete fun delete(item: RankEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(item: RankEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(list: List<RankEntity>)

    /**
     * [name] - unique category name
     */
    @Query(value = "SELECT * FROM RANK_TABLE WHERE RK_NAME = :name")
    operator fun get(name: String): RankEntity?

    @Query(value = "SELECT * FROM RANK_TABLE ORDER BY RK_POSITION ASC")
    fun get(): MutableList<RankEntity>

    @Query(value = "SELECT * FROM RANK_TABLE WHERE RK_ID IN(:idList) ORDER BY RK_POSITION ASC")
    operator fun get(idList: List<Long>): List<RankEntity>

    /**
     * Лист с id категорий, которые видны
     */
    @Query(value = "SELECT RK_ID FROM RANK_TABLE WHERE RK_VISIBLE = 1 ORDER BY RK_POSITION")
    fun getIdVisibleList(): List<Long>

    @Query(value = "SELECT COUNT(RK_ID) FROM RANK_TABLE")
    fun getCount(): Int

    @Query(value = "SELECT RK_NAME FROM RANK_TABLE ORDER BY RK_POSITION")
    fun getNameList(): List<String>

    @Query(value = "SELECT RK_ID FROM RANK_TABLE ORDER BY RK_POSITION")
    fun getIdList(): List<Long>


}