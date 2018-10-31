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
import sgtmelon.scriptum.office.annot.def.TypeDef;
import sgtmelon.scriptum.office.conv.BoolConv;
import sgtmelon.scriptum.office.conv.ListConv;

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
    abstract int getNoteCount(@TypeDef int type, Long[] noteId);

    @Update
    abstract void updateNote(List<NoteItem> listNote);

    /**
     * Обновление при удалении категории
     *
     * @param noteId - Id заметок принадлижащих к категории
     * @param rankId - Id категории, которую удалили
     */
    void updateNote(Long[] noteId, long rankId) {
        final  List<NoteItem> listNote = getNote(noteId);

        for (int i = 0; i < listNote.size(); i++) {
            final NoteItem noteItem = listNote.get(i);

            //Убирает из массивов ненужную категорию по id
            Long[] id = noteItem.getRankId();
            Long[] ps = noteItem.getRankPs();

            final List<Long> rankIdList = ListConv.toList(id);
            final List<Long> rankPsList = ListConv.toList(ps);

            final int index = rankIdList.indexOf(rankId);

            rankIdList.remove(index);
            rankPsList.remove(index);

            id = ListConv.fromList(rankIdList);
            ps = ListConv.fromList(rankPsList);

            noteItem.setRankId(id);
            noteItem.setRankPs(ps);

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
     * @param id - Массив с id категорий
     * @return - Список моделей категорий
     */
    @Query("SELECT * FROM RANK_TABLE " +
            "WHERE RK_ID IN(:id)" +
            "ORDER BY RK_POSITION ASC")
    abstract List<RankItem> getRank(Long[] id);

    @Update
    public abstract void updateRank(List<RankItem> listRank);

    /**
     * @param noteId - Id заметки, которую необходимо убрать из категории
     * @param rankId - Массив из id категорий, принадлежащих заметке
     */
    void clearRank(long noteId, Long[] rankId) {
        final List<RankItem> listRank = getRank(rankId);

        for (int i = 0; i < listRank.size(); i++) {
            RankItem rankItem = listRank.get(i);

            //Убирает из массива необходимую дату создания заметки
            Long[] id = rankItem.getIdNote();
            final List<Long> createList = ListConv.toList(id);
            createList.remove(noteId);
            id = ListConv.fromList(createList);

            rankItem.setIdNote(id);
            listRank.set(i, rankItem);
        }

        updateRank(listRank);
    }

}
