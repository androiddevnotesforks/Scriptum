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
     * @param noteCreate - Массив с датами создания заметок
     * @return - Список заметок по данным датам создания
     */
    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_CREATE IN(:noteCreate)")
    abstract List<ItemNote> getNote(String[] noteCreate);

    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_CREATE IN(:noteCreate)")
    abstract List<ItemNote> getNote(List<String> noteCreate);

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
     * @param noteCreate - Даты создания заметок принадлижащих к категории
     * @param noteRankId - Id категории, которую удалили
     */
    void updateNote(String[] noteCreate, String noteRankId) {
        List<ItemNote> listNote = getNote(noteCreate);

        for (int i = 0; i < listNote.size(); i++) {
            ItemNote itemNote = listNote.get(i);
            itemNote.removeRank(noteRankId);
            listNote.set(i, itemNote);
        }

        updateNote(listNote);
    }

    /**
     * @param noteCreate - Даты создания заметок
     * @return - Количество пунктов по датам создания заметок
     */
    @Query("SELECT COUNT(RL_ID) FROM ROLL_TABLE " +
            "WHERE RL_CREATE IN(:noteCreate)")
    abstract int getRollCount(String[] noteCreate);

    /**
     * @param noteCreate - Даты создания заметок
     * @return - Количество пунктов
     */
    @Query("SELECT COUNT(RL_ID) FROM ROLL_TABLE " +
            "WHERE RL_CHECK = 1 AND RL_CREATE IN(:noteCreate)")
    abstract int getRollCheck(String[] noteCreate);

    /**
     * @return - Лист с id категорий, которые видны
     */
    @Query("SELECT RK_ID FROM RANK_TABLE " +
            "WHERE RK_VISIBLE = 1 " +
            "ORDER BY RK_POSITION")
    abstract List<Integer> getRankVisible();

    /**
     * Удаление пунктов при удалении заметки
     *
     * @param noteCreate - Дата создания заметки
     */
    @Query("DELETE FROM ROLL_TABLE " +
            "WHERE RL_CREATE = :noteCreate")
    public abstract void deleteRoll(String noteCreate);

    /**
     * @param rankId - Массив с id категорий
     * @return - Список моделей категорий
     */
    @Query("SELECT * FROM RANK_TABLE " +
            "WHERE RK_ID IN(:rankId)" +
            "ORDER BY RK_POSITION ASC")
    abstract List<ItemRank> getRank(String[] rankId);

    /**
     * @param listRank - список категорий, которые необходимо обновить
     */
    @Update
    public abstract void updateRank(List<ItemRank> listRank);

    /**
     * @param noteCreate - Дата создания заметки, которую необходимо убрать из категории
     * @param rankId     - Массив в id категорий
     */
    void clearRank(String noteCreate, String[] rankId) {
        List<ItemRank> listRank = getRank(rankId);

        for (int i = 0; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);
            itemRank.removeCreate(noteCreate);
            listRank.set(i, itemRank);
        }

        updateRank(listRank);
    }

}
