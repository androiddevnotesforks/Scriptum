package sgtmelon.scriptum.app.database.dao;

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
import sgtmelon.scriptum.office.annot.def.StateDef;
import sgtmelon.scriptum.office.annot.def.TypeNoteDef;
import sgtmelon.scriptum.office.conv.BoolConv;

/**
 * Класс обеспечивающий базовую логику, которая используется в разных Dao
 * {@link NoteDao}, {@link RankDao}, {@link RollDao}
 */
@Dao
@TypeConverters({BoolConv.class})
abstract class BaseDao {

    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_ID IN(:id)")
    abstract List<NoteItem> getNote(Long[] id);

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
    List<NoteItem> getNote(@StateDef.Bin int bin, String sortKeys) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                "SELECT * FROM " + DbAnn.NT_TB +
                        " WHERE " + DbAnn.NT_BN + " = " + bin +
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
    abstract int getNoteCount(@TypeNoteDef int type, List<Long> noteId);

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

        for (int i = 0; i < listNote.size(); i++) {
            final NoteItem noteItem = listNote.get(i);

            //Убирает из списков ненужную категорию по id
            final List<Long> listRankId = noteItem.getRankId();
            final List<Long> listRankPs = noteItem.getRankPs();
            final int index = listRankId.indexOf(rankId);

            listRankId.remove(index);
            listRankPs.remove(index);

            noteItem.setRankId(listRankId);
            noteItem.setRankPs(listRankPs);

            listNote.set(i, noteItem);
        }

        updateNote(listNote);
    }

    /**
     * @return - Получение списка всех пунктов с позиции 0 по 3 (4 пунка)
     */
    @Query("SELECT * FROM ROLL_TABLE " +
            "WHERE RL_ID_NOTE = :idNote AND RL_POSITION BETWEEN 0 AND 3 " +
            "ORDER BY RL_POSITION ASC")
    abstract List<RollItem> getRollView(long idNote);

    @Query("SELECT * FROM ROLL_TABLE " +
            "WHERE RL_ID_NOTE = :idNote " +
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
     * @param noteId - Id заметки, которую необходимо убрать из категории
     * @param rankId - Массив из id категорий, принадлежащих заметке
     */
    void clearRank(long noteId, List<Long> rankId) {
        final List<RankItem> listRank = getRank(rankId);

        for (int i = 0; i < listRank.size(); i++) {
            final RankItem rankItem = listRank.get(i);

            //Убирает из списка необходимую дату создания заметки
            final List<Long> listId = rankItem.getIdNote();
            listId.remove(noteId);

            rankItem.setIdNote(listId);
            listRank.set(i, rankItem);
        }

        updateRank(listRank);
    }

}
