package sgtmelon.scriptum.app.room.dao

import androidx.room.*
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.room.RoomDb
import java.util.*

/**
 * Класс для общения Dao категорий [RoomDb]
 */
@Dao
interface RankDao : BaseDao {

    /**
     * Лист с id категорий, которые видны
     */
    @get:Query(value = "SELECT RK_ID FROM RANK_TABLE WHERE RK_VISIBLE = 1 ORDER BY RK_POSITION")
    val rankVisible: List<Long>

    @get:Query(value = "SELECT COUNT(RK_ID) FROM RANK_TABLE")
    val count: Long

    @get:Query(value = "SELECT * FROM RANK_TABLE ORDER BY RK_POSITION ASC")
    val simple: MutableList<RankItem>

    @Query(value = "SELECT * FROM RANK_TABLE WHERE RK_ID IN(:idList) ORDER BY RK_POSITION ASC")
    fun get(idList: List<Long>): List<RankItem>

    @get:Query(value = "SELECT RK_NAME FROM RANK_TABLE ORDER BY RK_POSITION")
    val name: Array<String>

    @get:Query(value = "SELECT RK_ID FROM RANK_TABLE ORDER BY RK_POSITION")
    val id: Array<Long>

    @Insert fun insert(rankItem: RankItem): Long

    /**
     * @param name - Уникальное имя категории
     * @return - Модель категории
     */
    @Query(value = "SELECT * FROM RANK_TABLE WHERE RK_NAME = :name")
    fun get(name: String): RankItem

    fun getCheck(id: List<Long>): BooleanArray {
        val rankList = simple

        val check = BooleanArray(rankList.size)
        for (i in rankList.indices) {
            val rankItem = rankList[i]
            check[i] = id.contains(rankItem.id)
        }

        return check
    }

    @Update fun update(rankItem: RankItem)

    @Update fun update(rankList: List<RankItem>)

    /**
     * @param p - Позиция удаления категории
     */
    fun update(p: Int) {
        val rankList = simple
        val noteIdList = ArrayList<Long>()

        for (i in p until rankList.size) {
            rankList[i].apply {
                noteId.forEach { if (!noteIdList.contains(it)) noteIdList.add(it) }
                position = i
            }
        }

        update(rankList)
        update(noteIdList, rankList)
    }

    /**
     * @param noteIdList - Id заметок, которые нужно обновить
     * @param rankList   - Новый список категорий, с новыми позициями у категорий
     */
    fun update(noteIdList: List<Long>, rankList: List<RankItem>) {
        val noteList = getNote(noteIdList)

        for (noteItem in noteList) {
            val oldIdList = noteItem.rankId

            val newIdList = ArrayList<Long>()
            val newPsList = ArrayList<Long>()

            for (rankItem in rankList) {
                val id = rankItem.id
                val ps = rankItem.position.toLong()

                if (oldIdList.contains(id)) {
                    newIdList.add(id)
                    newPsList.add(ps)
                }
            }

            noteItem.rankId = newIdList
            noteItem.rankPs = newPsList
        }

        updateNote(noteList)
    }

    @Delete fun delete(rankItem: RankItem)

}