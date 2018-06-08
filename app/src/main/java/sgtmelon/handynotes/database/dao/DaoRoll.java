package sgtmelon.handynotes.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.database.converter.ConverterBool;
import sgtmelon.handynotes.model.item.ItemRoll;
import sgtmelon.handynotes.model.item.ItemRollView;
import sgtmelon.handynotes.model.manager.ManagerRoll;

@Dao
@TypeConverters({ConverterBool.class})
public abstract class DaoRoll {

    @Insert
    public abstract long insertRoll(ItemRoll itemRoll);

    /**
     * Запись пунктов после конвертирования из текстовой заметки
     * @param rlCreate - Дата создания заметки
     * @param rlText - Массив потенциальных пунктов
     * @return - Для ManagerRoll и информации о размере
     */
    public ItemRollView insertRoll(String rlCreate, String[] rlText) {
        ItemRollView itemRollView = new ItemRollView();

        List<ItemRoll> listRoll = new ArrayList<>();
        int rollPs = 0;

        for (String aRollTx : rlText) {
            if (!aRollTx.equals("")) {

                ItemRoll itemRoll = new ItemRoll();
                itemRoll.setCreate(rlCreate);
                itemRoll.setPosition(rollPs);
                itemRoll.setCheck(false);
                itemRoll.setText(aRollTx);
                itemRoll.setExist(true);

                int rollId = (int) insertRoll(itemRoll);

                if (rollPs <= 3) {
                    itemRoll.setId(rollId);
                    listRoll.add(itemRoll);
                }
                rollPs++;
            }
        }

        itemRollView.setListRoll(listRoll);
        itemRollView.setSize(rollPs);

        return itemRollView;
    }

    @Query("SELECT * FROM ROLL_TABLE " +
            "WHERE RL_CREATE = :rlCreate " +
            "ORDER BY RL_POSITION")
    public abstract List<ItemRoll> getRoll(String rlCreate);

    /**
     * Получение списка всех пунктов с позиции 0 по 3 (4 пунка)
     * @return - Список пунктов
     */
    @Query("SELECT * FROM ROLL_TABLE " +
            "WHERE RL_POSITION BETWEEN 0 AND 3 " +
            "ORDER BY DATE(RL_CREATE) DESC, TIME(RL_CREATE) DESC, RL_POSITION ASC")
    public abstract List<ItemRoll> getRoll();

    public ManagerRoll getManagerRoll() {
        List<String> listNoteCreate = new ArrayList<>();
        List<ItemRollView> listRollView = new ArrayList<>();

        List<ItemRoll> listRollBetween = getRoll(); //TODO Другое имя

        List<ItemRoll> listRoll = new ArrayList<>();
        for (int i = 0; i < listRollBetween.size(); i++) {
            ItemRoll itemRoll = listRollBetween.get(i);
            itemRoll.setExist(true);

            if (!listNoteCreate.contains(itemRoll.getCreate())) {
                listNoteCreate.add(itemRoll.getCreate());

                if (listRoll.size() != 0) {
                    listRollView.add(new ItemRollView(listRoll));
                    listRoll = new ArrayList<>();
                }
            }

            listRoll.add(itemRoll);
        }

        if (listRoll.size() != 0) {
            listRollView.add(new ItemRollView(listRoll));
        }

        return new ManagerRoll(listNoteCreate, listRollView);
    }

    /**
     * Получение текста для текстовой заметки на основе списка
     * @param rlCreate - Дата создания заметки
     * @return - Строка для текстовой заметки
     */
    public String getRollText(String rlCreate) {
        List<ItemRoll> listRoll = getRoll(rlCreate);

        StringBuilder rollTx = new StringBuilder();
        for (int i = 0; i < listRoll.size(); i++) {
            if (i != 0) rollTx.append("\n");
            rollTx.append(listRoll.get(i).getText());
        }

        return rollTx.toString();
    }

    /**
     * Получение текста для уведомления на основе списка
     *
     * @param rlCreate - Дата создания заметки
     * @param rlCheck  - Количество отмеченых пунктов в заметке
     * @return - Строка для уведомления
     */
    public String getRollText(String rlCreate, String rlCheck) {
        List<ItemRoll> listRoll = getRoll(rlCreate);

        StringBuilder rollTx = new StringBuilder();
        rollTx.append(rlCheck).append(" |");

        for (int i = 0; i < listRoll.size(); i++) {
            ItemRoll itemRoll = listRoll.get(i);

            if (itemRoll.isCheck()) rollTx.append(" \u2713 ");
            else rollTx.append(" - ");

            rollTx.append(itemRoll.getText());

            if (i != listRoll.size() - 1) rollTx.append(" |");
        }

        return rollTx.toString();
    }

    @Query("UPDATE ROLL_TABLE " +
            "SET RL_POSITION = :rlPosition, RL_TEXT = :rlText " +
            "WHERE RL_ID = :rlId")
    public abstract void updateRoll(int rlId, int rlPosition, String rlText);

    /**
     * Обновление выполнения конкретного пункта
     * @param rlId - Id пункта
     * @param rlCheck - Состояние отметки
     */
    @Query("UPDATE ROLL_TABLE " +
            "SET RL_CHECK = :rlCheck " +
            "WHERE RL_ID = :rlId")
    public abstract void updateRoll(int rlId, boolean rlCheck);

    /**
     * Обновление выполнения для всех пунктов
     * @param rlCreate - Дата создания заметки
     * @param rlCheck - Состояние отметки
     */
    @Query("UPDATE ROLL_TABLE " +
            "SET RL_CHECK = :rlCheck " +
            "WHERE RL_CREATE = :rlCreate")
    public abstract void updateRoll(String rlCreate, int rlCheck);

    /**
     * Удаление пунктов при сохранении после свайпа
     * @param rlCreate - Дата создания заметки
     * @param notSwipeId - Id, которые остались в заметке
     */
    @Query("DELETE FROM ROLL_TABLE " +
            "WHERE RL_CREATE = :rlCreate AND RL_ID NOT IN (:notSwipeId)")
    public abstract void deleteRoll(String rlCreate, String[] notSwipeId);

    /**
     * Удаление пунктов при удалении заметки
     * @param rlCreate - Дата создания заметки
     */
    @Query("DELETE FROM ROLL_TABLE " +
            "WHERE RL_CREATE = :rlCreate")
    public abstract void deleteRoll(String rlCreate);

}
