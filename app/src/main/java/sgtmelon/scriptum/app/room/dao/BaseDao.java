package sgtmelon.scriptum.app.room.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.TypeConverters;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RankItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.converter.BoolConverter;
import sgtmelon.scriptum.office.converter.NoteTypeConverter;

/**
 * Класс обеспечивающий базовую логику, которая используется в разных Dao
 * {@link NoteDao}, {@link RankDao}, {@link RollDao}
 */
@Dao
@TypeConverters({BoolConverter.class, NoteTypeConverter.class})
abstract class BaseDao {

    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_ID IN(:id)")
    abstract List<NoteItem> getNote(List<Long> id);

    @RawQuery
    abstract List<NoteItem> getNote(SupportSQLiteQuery query);

    /**
     * @param bin      - Расположение заметок относительно корзины
     * @param sortKeys - Строка с сортировкой заметок
     * @return - Список заметок
     */
    List<NoteItem> getNote(boolean bin, String sortKeys) {

        final SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                "SELECT * FROM " + DbAnn.Note.TABLE +
                        " WHERE " + DbAnn.Note.BIN + " = " + new BoolConverter().fromBool(bin) + // TODO убрать
                        " ORDER BY " + sortKeys);

        return getNote(query);
    }

    /**
     * @param type   - Тип заметки
     * @param noteId - Id заметок
     * @return - Количество заметок по датам создания
     */
    @Query("SELECT COUNT(NT_ID) FROM NOTE_TABLE " +
            "WHERE NT_TYPE = :type AND NT_ID IN(:noteId)")
    abstract int getNoteCount(int type, List<Long> noteId);

    @Update
    abstract void updateNote(List<NoteItem> listNote);

    /**
     * Обновление при удалении категории
     *
     * @param noteId - Id заметок принадлижащих к категории
     * @param rankId - Id категории, которую удалили
     */
    void updateNote(List<Long> noteId, long rankId) {
        final List<NoteItem> listNote = getNote(noteId);

        for (NoteItem noteItem : listNote) {
            //Убирает из списков ненужную категорию по id
            final List<Long> listRankId = noteItem.getRankId();
            final List<Long> listRankPs = noteItem.getRankPs();

            final int index = listRankId.indexOf(rankId);

            listRankId.remove(index);
            listRankPs.remove(index);
        }

        updateNote(listNote);
    }

    /**
     * @return - Получение списка всех пунктов с позиции 0 по 3 (4 пунка)
     */
    @Query("SELECT * FROM ROLL_TABLE " +
            "WHERE RL_NOTE_ID = :idNote AND RL_POSITION BETWEEN 0 AND 3 " +
            "ORDER BY RL_POSITION ASC")
    abstract List<RollItem> getRollView(long idNote);

    @Query("SELECT * FROM ROLL_TABLE " +
            "WHERE RL_NOTE_ID = :idNote " +
            "ORDER BY RL_POSITION")
    abstract List<RollItem> getRoll(long idNote);

    /**
     * @return - Лист с id категорий, которые видны
     */
    @Query("SELECT RK_ID FROM RANK_TABLE " +
            "WHERE RK_VISIBLE = 1 " +
            "ORDER BY RK_POSITION")
    public abstract List<Long> getRankVisible();

    /**
     * @param id - Список с id категорий
     * @return - Список моделей категорий
     */
    @Query("SELECT * FROM RANK_TABLE " +
            "WHERE RK_ID IN(:id)" +
            "ORDER BY RK_POSITION ASC")
    abstract List<RankItem> getRank(List<Long> id);

    @Update
    public abstract void updateRank(List<RankItem> listRank);

    /**
     * @param noteId     - Id заметки, которую необходимо убрать из категории
     * @param listRankId - Массив из id категорий, принадлежащих заметке
     */
    void clearRank(long noteId, List<Long> listRankId) {
        final List<RankItem> listRank = getRank(listRankId);

        for (RankItem rankItem : listRank) {
            rankItem.getNoteId().remove(noteId);
        }

        updateRank(listRank);
    }

}