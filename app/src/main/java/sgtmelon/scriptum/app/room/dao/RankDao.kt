package sgtmelon.scriptum.app.room.dao

import androidx.room.*
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.app.room.converter.BoolConverter

/**
 * Класс для общения Dao категорий [RoomDb]
 *
 * @author SerjantArbuz
 * @version 1.0
 */
@Dao
@TypeConverters(BoolConverter::class)
interface RankDao {

    /**
     * Лист с id категорий, которые видны
     */
    @get:Query(value = "SELECT RK_ID FROM RANK_TABLE WHERE RK_VISIBLE = 1 ORDER BY RK_POSITION")
    val rankIdVisibleList: List<Long>

    @get:Query(value = "SELECT COUNT(RK_ID) FROM RANK_TABLE")
    val count: Long

    @get:Query(value = "SELECT * FROM RANK_TABLE ORDER BY RK_POSITION ASC")
    val simple: MutableList<RankItem>

    @Query(value = "SELECT * FROM RANK_TABLE WHERE RK_ID IN(:idList) ORDER BY RK_POSITION ASC")
    operator fun get(idList: List<Long>): List<RankItem>

    @get:Query(value = "SELECT RK_NAME FROM RANK_TABLE ORDER BY RK_POSITION")
    val name: List<String>

    @get:Query(value = "SELECT RK_ID FROM RANK_TABLE ORDER BY RK_POSITION")
    val id: List<Long>

    @Insert fun insert(rankItem: RankItem): Long

    /**
     * @param name - Уникальное имя категории
     * @return - Модель категории
     */
    @Query(value = "SELECT * FROM RANK_TABLE WHERE RK_NAME = :name")
    operator fun get(name: String): RankItem

    @Update fun update(rankItem: RankItem)

    @Update fun update(rankList: List<RankItem>)

    @Delete fun delete(rankItem: RankItem)

}