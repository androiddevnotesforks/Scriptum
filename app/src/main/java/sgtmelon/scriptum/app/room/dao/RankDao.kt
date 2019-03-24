package sgtmelon.scriptum.app.room.dao

import androidx.room.*
import sgtmelon.scriptum.app.model.RankModel
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.app.room.converter.NoteTypeConverter
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

    private val complex: MutableList<RankItem>
        get() {
            val rankList = simple

            for (rankItem in rankList) {
                val noteIdList = rankItem.noteId

                rankItem.textCount = getNoteCount(NoteTypeConverter().toInt(NoteType.TEXT), noteIdList)
                rankItem.rollCount = getNoteCount(NoteTypeConverter().toInt(NoteType.ROLL), noteIdList)
            }

            return rankList
        }

    @get:Query(value = "SELECT RK_NAME FROM RANK_TABLE ORDER BY RK_POSITION")
    val name: Array<String>

    @get:Query(value = "SELECT RK_ID FROM RANK_TABLE ORDER BY RK_POSITION")
    val id: Array<Long>

    @Insert fun insert(rankItem: RankItem): Long

    fun get(): RankModel {
        val rankList = complex

        val nameList = ArrayList<String>()
        for (item in rankList) {
            nameList.add(item.name.toUpperCase())
        }

        return RankModel(rankList, nameList)
    }

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

    /**
     * Добавление или удаление id заметки к категорииё
     *
     * @param noteId     - Id заметки
     * @param rankIdList - Id категорий принадлежащих каметке
     */
    fun update(noteId: Long, rankIdList: List<Long>) {
        val rankList = simple
        val check = getCheck(rankIdList)

        for (i in rankList.indices) {
            val rankItem = rankList[i]
            val noteIdList = rankItem.noteId

            if (check[i] && !noteIdList.contains(noteId)) {
                noteIdList.add(noteId)
            } else if (!check[i]) {
                noteIdList.remove(noteId)
            }
        }

        update(rankList)
    }

    @Update fun update(rankItem: RankItem)

    @Update fun update(rankList: List<RankItem>)

    /**
     * @param dragFrom - Начальная позиция обновления
     * @param dragTo   - Конечная позиция обновления
     */
    fun update(dragFrom: Int, dragTo: Int): List<RankItem> {
        val startFirst = dragFrom < dragTo

        val iStart = if (startFirst) dragFrom else dragTo
        val iEnd = if (startFirst) dragTo else dragFrom
        val iAdd = if (startFirst) -1 else 1

        val rankList = complex
        val noteIdList = ArrayList<Long>()

        for (i in iStart..iEnd) {
            val rankItem = rankList[i]

            for (id in rankItem.noteId) {
                if (!noteIdList.contains(id)) {
                    noteIdList.add(id)
                }
            }

            val start = i == dragFrom
            val end = i == dragTo

            val newPosition = if (start) dragTo else i + iAdd
            rankItem.position = newPosition

            if (startFirst) {
                if (end) {
                    rankList.removeAt(i)
                    rankList.add(newPosition, rankItem)
                } else {
                    rankList[i] = rankItem
                }
            } else {
                if (start) {
                    rankList.removeAt(i)
                    rankList.add(newPosition, rankItem)
                } else {
                    rankList[i] = rankItem
                }
            }
        }

        if (rankList[0].position != 0) {
            rankList.reverse()
        }

        update(rankList)
        update(noteIdList, rankList)

        return rankList
    }

    /**
     * @param position - Позиция удаления категории
     */
    fun update(position: Int) {
        val rankList = simple
        val noteIdList = ArrayList<Long>()

        for (i in position until rankList.size) {
            val rankItem = rankList[i]

            for (id in rankItem.noteId) {
                if (!noteIdList.contains(id)) {
                    noteIdList.add(id)
                }
            }

            rankItem.position = i
        }

        update(rankList)
        update(noteIdList, rankList)
    }

    /**
     * @param noteIdList - Id заметок, которые нужно обновить
     * @param rankList   - Новый список категорий, с новыми позициями у категорий
     */
    private fun update(noteIdList: List<Long>, rankList: List<RankItem>) {
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