package sgtmelon.handynotes.app.dataBase.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.TypeConverters;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRank;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.office.annot.def.db.DefBin;
import sgtmelon.handynotes.office.annot.def.db.DefDb;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.conv.ConvBool;

/**
 * Класс обеспечивающий базовую логику, которая используется в разных Dao
 */
@Dao
@TypeConverters({ConvBool.class})
abstract class DaoBase {

    //region NoteBase

    /**
     * @param id - Массив с датами создания заметок
     * @return - Список заметок по данным датам создания
     */
    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_ID IN(:id)")
    abstract List<ItemNote> getNote(Long[] id);

    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_ID IN(:id)")
    abstract List<ItemNote> getNote(List<Long> id);

    @RawQuery
    abstract List<ItemNote> getNote(SupportSQLiteQuery query);

    /**
     * @param bin      - Расположение заметок
     * @param sortKeys - Строка с сортировкой заметок
     * @return - Список заметок
     */
    List<ItemNote> getNote(@DefBin int bin, String sortKeys) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                "SELECT * FROM " + DefDb.NT_TB +
                        " WHERE " + DefDb.NT_BN + " = " + bin +
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
    abstract int getNoteCount(@DefType int type, Long[] noteId);

    @Update
    abstract void updateNote(List<ItemNote> listNote);

    /**
     * Обновление при удалении категории
     *
     * @param noteId - Id заметок принадлижащих к категории
     * @param rankId - Id категории, которую удалили
     */
    void updateNote(Long[] noteId, long rankId) {
        List<ItemNote> listNote = getNote(noteId);

        for (int i = 0; i < listNote.size(); i++) {
            ItemNote itemNote = listNote.get(i);
            itemNote.removeRank(rankId);
            listNote.set(i, itemNote);
        }

        updateNote(listNote);
    }

    //endregion

    //region RollBase

    /**
     * @return - Получение списка всех пунктов с позиции 0 по 3 (4 пунка)
     */
    @Query("SELECT * FROM ROLL_TABLE " +
            "WHERE RL_ID_NOTE = :idNote AND RL_POSITION BETWEEN 0 AND 3 " +
            "ORDER BY RL_POSITION ASC")
    abstract List<ItemRoll> getRollView(long idNote);

    @Query("SELECT * FROM ROLL_TABLE " +
            "WHERE RL_ID_NOTE = :idNote " +
            "ORDER BY RL_POSITION")
    abstract List<ItemRoll> getRoll(long idNote);

    /**
     * @param idNote - Id заметок
     * @return - Количество пунктов по датам создания заметок
     */
    @Query("SELECT COUNT(RL_ID) FROM ROLL_TABLE " +
            "WHERE RL_ID_NOTE IN(:idNote)")
    abstract int getRollCount(Long[] idNote);

    /**
     * @param idNote - Id заметок
     * @return - Количество пунктов, которые отмечены
     */
    @Query("SELECT COUNT(RL_ID) FROM ROLL_TABLE " +
            "WHERE RL_CHECK = 1 AND RL_ID_NOTE IN(:idNote)")
    abstract int getRollCheck(Long[] idNote);

    //endregion

    //region RankBase

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
    abstract List<ItemRank> getRank(Long[] id);

    /**
     * @param listRank - список категорий, которые необходимо обновить
     */
    @Update
    public abstract void updateRank(List<ItemRank> listRank);

    /**
     * @param noteId - Id заметки, которую необходимо убрать из категории
     * @param rankId - Массив из id категорий, принадлежащих заметке
     */
    void clearRank(long noteId, Long[] rankId) {
        List<ItemRank> listRank = getRank(rankId);

        for (int i = 0; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);
            itemRank.removeId(noteId);
            listRank.set(i, itemRank);
        }

        updateRank(listRank);
    }

    //endregion

}
