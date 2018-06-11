package sgtmelon.handynotes.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sgtmelon.handynotes.model.item.ItemNote;
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
    abstract List<ItemRank> getRankSimple();

    /**
     * @return - Список моделей категорий с указанной информацией
     */
    public List<ItemRank> getRank() {
        List<ItemRank> listRank = getRankSimple();

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
    public abstract List<Integer> getRankId();

    public boolean[] getRankCheck(String[] rankId) {
        List<ItemRank> listRank = getRankSimple();

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

    //TODO сократи перебор
    public void updateRank(int startPosition, int endPosition) {
        List<ItemRank> listRank = getRankSimple();

        int position;
        List<String> ntCreate = new ArrayList<>();

        if (startPosition < endPosition) {
            for (int i = startPosition; i <= endPosition; i++) {
                ItemRank itemRank = listRank.get(i);

                if (i == startPosition) position = endPosition;
                else position = i - 1;

                itemRank.setPosition(position);
                listRank.set(i, itemRank);

                for (String rankCreate : itemRank.getCreate()) {
                    if (!ntCreate.contains(rankCreate)) {
                        ntCreate.add(rankCreate);
                    }
                }
            }
        } else {
            for (int i = endPosition; i <= startPosition; i++) {
                ItemRank itemRank = listRank.get(i);

                if (i == startPosition) position = endPosition;
                else position = i + 1;

                itemRank.setPosition(position);
                listRank.set(i, itemRank);

                for (String rankCreate : itemRank.getCreate()) {
                    if (!ntCreate.contains(rankCreate)) {
                        ntCreate.add(rankCreate);
                    }
                }
            }
        }

        updateRank(listRank);
        List<ItemNote> listNote = getNote(ntCreate);

        for (int i = 0; i < listNote.size(); i++) {
            ItemNote itemNote = listNote.get(i);

            String[] rankIdOld = itemNote.getRankId();
            String[] rankIdNew = new String[rankIdOld.length];
            String[] rankPsNew = new String[rankIdOld.length];

            int ps = 0;

            for (int j = 0; j < listRank.size(); j++) {
                ItemRank itemRank = listRank.get(j);

                String rankId = Integer.toString(itemRank.getId());
                String rankPs = Integer.toString(itemRank.getPosition());

                if (Arrays.asList(rankIdOld).contains(rankId)) {
                    rankIdNew[ps] = rankId;
                    rankPsNew[ps] = rankPs;
                    ps++;
                }
            }

            itemNote.setRankId(rankIdNew);
            itemNote.setRankPs(rankPsNew);

            listNote.set(i, itemNote);
        }

        updateNote(listNote);
    }

    /**
     * @param startPosition - Позиция удаления категории
     */
    public void updateRank(int startPosition) {
        List<ItemRank> listRank = getRankSimple();

        for (int i = startPosition; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);
            itemRank.setPosition(i);
            listRank.set(i, itemRank);
        }

        updateRank(listRank);
    }

    @Delete
    abstract void deleteRank(ItemRank itemRank);

    public void deleteRank(String rankName) {
        ItemRank itemRank = getRank(rankName);

        String[] rankCreate = itemRank.getCreate();
        if (rankCreate.length != 0) updateNote(rankCreate, Integer.toString(itemRank.getId()));

        deleteRank(itemRank);
    }

    public void listAllRank(TextView textView) {
        List<ItemRank> listRank = getRank();

        String annotation = "Rank Data Base: ";
        textView.setText(annotation);

        for (int i = 0; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);

            textView.append("\n\n" +
                    "ID: " + itemRank.getId() + " | " +
                    "PS: " + itemRank.getPosition() + "\n" +
                    "NM: " + itemRank.getName() + "\n" +
                    "CR: " + TextUtils.join(", ", itemRank.getCreate()) + "\n" +
                    "VS: " + itemRank.isVisible());
        }
    }
}
