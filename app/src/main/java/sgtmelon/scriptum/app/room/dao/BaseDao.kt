package sgtmelon.scriptum.app.room.dao

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.room.converter.BoolConverter
import sgtmelon.scriptum.app.room.converter.NoteTypeConverter
import sgtmelon.scriptum.office.annot.DbAnn

/**
 * Класс обеспечивающий базовую логику, которая используется в разных Dao
 * [NoteDao], [RankDao], [RollDao]
 */
@Dao
@TypeConverters(BoolConverter::class, NoteTypeConverter::class)
interface BaseDao {

    /**
     * @return - Лист с id категорий, которые видны
     */
    @get:Query(value = "SELECT RK_ID FROM RANK_TABLE " +
            "WHERE RK_VISIBLE = 1 " +
            "ORDER BY RK_POSITION")
    val rankVisible: List<Long>

    @Query(value = "SELECT * FROM NOTE_TABLE " + "WHERE NT_ID IN(:id)")
    fun getNote(id: List<Long>): List<NoteItem>

    @RawQuery
    fun getNote(query: SupportSQLiteQuery): List<NoteItem>

    /**
     * @param bin      - Расположение заметок относительно корзины
     * @param sortKeys - Строка с сортировкой заметок
     * @return - Список заметок
     */
    fun getNote(bin: Boolean, sortKeys: String): List<NoteItem> {
        val query = SimpleSQLiteQuery(
                "SELECT * FROM " + DbAnn.Note.TABLE +
                        " WHERE " + DbAnn.Note.BIN + " = " + BoolConverter().toInt(bin) +
                        " ORDER BY " + sortKeys)

        // TODO убрать

        return getNote(query)
    }

    /**
     * @param type   - Тип заметки
     * @param noteId - Id заметок
     * @return - Количество заметок по датам создания
     */
    @Query(value = "SELECT COUNT(NT_ID) FROM NOTE_TABLE WHERE NT_TYPE = :type AND NT_ID IN(:noteId)")
    fun getNoteCount(type: Int, noteId: List<Long>): Int

    @Update
    fun updateNote(noteList: List<NoteItem>)

    /**
     * Обновление при удалении категории
     *
     * @param noteIdList - Id заметок принадлижащих к категории
     * @param rankId - Id категории, которую удалили
     */
    fun updateNote(noteIdList: List<Long>, rankId: Long) {
        val noteList = getNote(noteIdList)

        for (noteItem in noteList) {
            //Убирает из списков ненужную категорию по id
            val rankIdList = noteItem.rankId
            val rankPsList = noteItem.rankPs

            val index = rankIdList.indexOf(rankId)

            rankIdList.removeAt(index)
            rankPsList.removeAt(index)
        }

        updateNote(noteList)
    }

    /**
     * @return - Получение списка всех пунктов с позиции 0 по 3 (4 пунка)
     */
    @Query(value = "SELECT * FROM ROLL_TABLE " +
            "WHERE RL_NOTE_ID = :idNote AND RL_POSITION BETWEEN 0 AND 3 " +
            "ORDER BY RL_POSITION ASC")
    fun getRollView(idNote: Long): MutableList<RollItem>

    @Query(value = "SELECT * FROM ROLL_TABLE " +
            "WHERE RL_NOTE_ID = :idNote " +
            "ORDER BY RL_POSITION")
    fun getRoll(idNote: Long):  MutableList<RollItem>

    /**
     * @param id - Список с id категорий
     * @return - Список моделей категорий
     */
    @Query(value = "SELECT * FROM RANK_TABLE " +
            "WHERE RK_ID IN(:id)" +
            "ORDER BY RK_POSITION ASC")
    fun getRank(id: List<Long>): List<RankItem>

    @Update
    fun updateRank(rankList: List<RankItem>)

    /**
     * @param noteId     - Id заметки, которую необходимо убрать из категории
     * @param rankIdList - Массив из id категорий, принадлежащих заметке
     */
    fun clearRank(noteId: Long, rankIdList: List<Long>) {
        val rankList = getRank(rankIdList)

        for (rankItem in rankList) {
            rankItem.noteId.remove(noteId)
        }

        updateRank(rankList)
    }

}