package sgtmelon.handynotes.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Arrays;
import java.util.List;

import sgtmelon.handynotes.model.item.ItemRank;
import sgtmelon.handynotes.service.Help;

@Dao
public abstract class DaoRank extends DaoBase {

    @Query("SELECT COUNT(RK_ID) FROM RANK_TABLE")
    public abstract long getRankCount();

    @Insert
    public abstract long insertRank(ItemRank itemRank);

    //TODO переименуй
    @Query("SELECT * FROM RANK_TABLE " +
            "ORDER BY RK_POSITION")
    abstract List<ItemRank> getRankWithoutInfo();

    /**
     * @return - Список моделей категорий с указанной информацией
     */
    public List<ItemRank> getRank() {
        List<ItemRank> listRank = getRankWithoutInfo();

        for (int i = 0; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);
            String[] rankCreate = itemRank.getCreate();

            itemRank.setTextCount(getNoteCount(typeText, rankCreate));
            itemRank.setRollCount(getNoteCount(typeRoll, rankCreate));
            itemRank.setRollCheck(getRollCount(rankCreate), getRollCount(true, rankCreate));

            listRank.set(i, itemRank);
        }

        return listRank;
    }

    /**
     * @param rankName - Уникальное имя категории
     * @return - Модель категории
     */
    @Query("SELECT * FROM RANK_TABLE " +
            "WHERE RK_NAME = :rankName")
    abstract ItemRank getRank(String rankName);

    /**
     * @return - Лист с именами в высоком регистре
     */
    @Query("SELECT UPPER(RK_NAME) FROM RANK_TABLE " +
            "ORDER BY RK_POSITION")
    public abstract List<String> getRankNameUpper();

    @Query("SELECT RK_NAME FROM RANK_TABLE " +
            "ORDER BY RK_POSITION")
    public abstract List<String> getRankName();

    @Query("SELECT RK_ID FROM RANK_TABLE " +
            "ORDER BY RK_POSITION")
    public abstract List<String> getRankId();

    public boolean[] getRankCheck(String[] rankId) {
        List<ItemRank> listRank = getRankWithoutInfo();

        boolean[] rankCheck = new boolean[listRank.size()];
        for (int i = 0; i < listRank.size(); i++) {
            String id = Integer.toString(listRank.get(i).getId());
            rankCheck[i] = Arrays.asList(rankId).contains(id);
        }

        return rankCheck;
    }

    //TODO проверь правильность работы
    public void updateRank(String noteCreate, String[] noteRankId) {
        List<ItemRank> listRank = getRank();

        boolean[] rankCheck = getRankCheck(noteRankId);
        for (int i = 0; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);
            List<String> rankCreate = Help.Array.strArrToList(itemRank.getCreate());

            if (rankCheck[i]) {
                if (!rankCreate.contains(noteCreate)) rankCreate.add(noteCreate);
            } else {
                if (rankCreate.contains(noteCreate)) rankCreate.remove(noteCreate);
            }

            listRank.set(i, itemRank);
        }

        updateRank(listRank);
    }

    @Update
    public abstract void updateRank(ItemRank itemRank);

    @Delete
    abstract void deleteRank(ItemRank itemRank);

    public void deleteRank(String rankName) {
        ItemRank itemRank = getRank(rankName);

        String[] rankCreate = itemRank.getCreate();
        if (rankCreate.length != 0) updateNote(rankCreate, Integer.toString(itemRank.getId()));

        deleteRank(itemRank);
    }
}
