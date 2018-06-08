package sgtmelon.handynotes.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.List;

import sgtmelon.handynotes.database.DataBaseDescription;
import sgtmelon.handynotes.database.converter.ConverterBool;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.item.ItemRank;

@Dao
@TypeConverters({ConverterBool.class})
abstract class DaoBase extends DataBaseDescription {

    /**
     * @param noteCreate - Массив с датами создания заметок
     * @return - Список заметок по данным датам создания
     */
    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_CREATE IN(:noteCreate)")
    abstract List<ItemNote> getNote(String[] noteCreate);

    /**
     * @param noteType   - Тип заметки
     * @param noteCreate - Даты создания заметок
     * @return - Количество заметок по датам создания
     */
    @Query("SELECT COUNT(NT_ID) FROM NOTE_TABLE " +
            "WHERE NT_TYPE = :noteType AND NT_CREATE IN(:noteCreate)")
    abstract int getNoteCount(int noteType, String[] noteCreate);

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
     * @param rollCheck  - Состояние выполнения пункта
     * @param noteCreate - Даты создания заметок
     * @return - Количество пунктов
     */
    @Query("SELECT COUNT(RL_ID) FROM ROLL_TABLE " +
            "WHERE RL_CHECK = :rollCheck AND RL_CREATE IN(:noteCreate)")
    abstract int getRollCount(boolean rollCheck, String[] noteCreate);

    /**
     * @return - Лист с id категорий, которые видны
     */
    @Query("SELECT RK_ID FROM RANK_TABLE " +
            "WHERE RK_VISIBLE = 1 " +
            "ORDER BY RK_POSITION")
    abstract List<String> getRankVisible();

    /**
     * Удаление пунктов при удалении заметки
     *
     * @param noteCreate - Дата создания заметки
     */
    @Query("DELETE FROM ROLL_TABLE " +
            "WHERE RL_CREATE = :noteCreate")
    public abstract void deleteRoll(String noteCreate);

    @Query("SELECT * FROM RANK_TABLE " +
            "WHERE RK_ID IN(:rankId)" +
            "ORDER BY RK_POSITION ASC")
    abstract List<ItemRank> getRank(String[] rankId);

    @Update
    public abstract void updateRank(List<ItemRank> listRank);

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
