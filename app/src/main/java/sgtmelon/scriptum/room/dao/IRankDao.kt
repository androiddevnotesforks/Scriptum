package sgtmelon.scriptum.room.dao

import androidx.room.*
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Interface for communications [DbData.Rank.TABLE] with [RoomDb]
 */
@Dao
@TypeConverters(BoolConverter::class)
interface IRankDao {

    fun insert(name: String) = insert(RankEntity(name = name))

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(rankEntity: RankEntity): Long

    @Query(value = "DELETE FROM RANK_TABLE WHERE RK_NAME = :name")
    fun delete(name: String)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(rankEntity: RankEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(list: List<RankEntity>)

    @Query(value = "SELECT * FROM RANK_TABLE WHERE RK_ID == :id")
    operator fun get(id: Long): RankItem?

    @Query(value = "SELECT * FROM RANK_TABLE ORDER BY RK_POSITION ASC")
    fun get(): MutableList<RankItem>

    @Query(value = "SELECT RK_ID FROM RANK_TABLE WHERE RK_VISIBLE = 1 ORDER BY RK_POSITION")
    fun getIdVisibleList(): List<Long>

    @Query(value = "SELECT COUNT(RK_ID) FROM RANK_TABLE")
    fun getCount(): Int

    @Query(value = "SELECT RK_NAME FROM RANK_TABLE ORDER BY RK_POSITION")
    fun getNameList(): List<String>

    @Query(value = "SELECT RK_ID FROM RANK_TABLE ORDER BY RK_POSITION")
    fun getIdList(): List<Long>

}