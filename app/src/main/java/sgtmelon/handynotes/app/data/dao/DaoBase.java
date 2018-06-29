package sgtmelon.handynotes.app.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.List;

import sgtmelon.handynotes.app.data.DataInfo;
import sgtmelon.handynotes.office.conv.ConvBool;
import sgtmelon.handynotes.office.def.data.DefType;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRank;

/**
 * Класс обеспечивающий базовую логику, которая используется в разных Dao
 */
@Dao
@TypeConverters({ConvBool.class})
abstract class DaoBase extends DataInfo {

    /**
     * @param noteId - Массив с датами создания заметок
     * @return - Список заметок по данным датам создания
     */
    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_ID IN(:noteId)")
    abstract List<ItemNote> getNote(Long[] noteId);

    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_ID IN(:noteId)")
    abstract List<ItemNote> getNote(List<Long> noteId);

    /**
     * @param noteType   - Тип заметки
     * @param noteCreate - Даты создания заметок
     * @return - Количество заметок по датам создания
     */
    @Query("SELECT COUNT(NT_ID) FROM NOTE_TABLE " +
            "WHERE NT_TYPE = :noteType AND NT_CREATE IN(:noteCreate)")
    abstract int getNoteCount(@DefType int noteType, String[] noteCreate);

    @Update
    abstract void updateNote(List<ItemNote> listNote);

    /**
     * Обновление при удалении категории
     *
     * @param noteId     - Id заметок принадлижащих к категории
     * @param noteRankId - Id категории, которую удалили
     */
    void updateNote(Long[] noteId, long noteRankId) {
        List<ItemNote> listNote = getNote(noteId);

        for (int i = 0; i < listNote.size(); i++) {
            ItemNote itemNote = listNote.get(i);
            itemNote.removeRank(noteRankId);
            listNote.set(i, itemNote);
        }

        updateNote(listNote);
    }

    /**
     * @param rollIdNote - Id заметок
     * @return - Количество пунктов по датам создания заметок
     */
    @Query("SELECT COUNT(RL_ID) FROM ROLL_TABLE " +
            "WHERE RL_ID_NOTE IN(:rollIdNote)")
    abstract int getRollCount(long[] rollIdNote);

    /**
     * @param rollIdNote - Id заметок
     * @return - Количество пунктов
     */
    @Query("SELECT COUNT(RL_ID) FROM ROLL_TABLE " +
            "WHERE RL_CHECK = 1 AND RL_ID_NOTE IN(:rollIdNote)")
    abstract int getRollCheck(long[] rollIdNote);

    /**
     * @return - Лист с id категорий, которые видны
     */
    @Query("SELECT RK_ID FROM RANK_TABLE " +
            "WHERE RK_VISIBLE = 1 " +
            "ORDER BY RK_POSITION")
    public abstract List<Long> getRankVisible();

    /**
     * Удаление пунктов при удалении заметки
     *
     * @param rollIdNote - Id заметки
     */
    @Query("DELETE FROM ROLL_TABLE " +
            "WHERE RL_ID_NOTE = :rollIdNote")
    public abstract void deleteRoll(long rollIdNote);

    /**
     * @param rankId - Массив с id категорий
     * @return - Список моделей категорий
     */
    @Query("SELECT * FROM RANK_TABLE " +
            "WHERE RK_ID IN(:rankId)" +
            "ORDER BY RK_POSITION ASC")
    abstract List<ItemRank> getRank(Long[] rankId);

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

}
