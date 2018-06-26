package sgtmelon.handynotes.app.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.office.conv.ConvBool;
import sgtmelon.handynotes.office.def.data.DefCheck;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.item.ItemRollView;
import sgtmelon.handynotes.app.model.manager.ManagerRoll;

@Dao
@TypeConverters({ConvBool.class})
public abstract class DaoRoll extends DaoBase {

    @Insert
    public abstract long insert(ItemRoll itemRoll);

    /**
     * Запись пунктов после конвертирования из текстовой заметки
     *
     * @param rollCreate - Дата создания заметки
     * @param rollText   - Массив потенциальных пунктов
     * @return - Для ManagerRoll и информации о размере
     */
    public ItemRollView insert(String rollCreate, String[] rollText) {
        ItemRollView itemRollView = new ItemRollView();

        List<ItemRoll> listRoll = new ArrayList<>();
        int rollPs = 0;

        for (String aRollTx : rollText) {
            if (!aRollTx.equals("")) {

                ItemRoll itemRoll = new ItemRoll();
                itemRoll.setCreate(rollCreate);
                itemRoll.setPosition(rollPs);
                itemRoll.setCheck(false);
                itemRoll.setText(aRollTx);
                itemRoll.setExist(true);

                int rollId = (int) insert(itemRoll);

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
            "WHERE RL_CREATE = :rollCreate " +
            "ORDER BY RL_POSITION")
    public abstract List<ItemRoll> get(String rollCreate);

    /**
     * Получение списка всех пунктов с позиции 0 по 3 (4 пунка)
     *
     * @return - Список пунктов
     */
    @Query("SELECT * FROM ROLL_TABLE " +
            "WHERE RL_POSITION BETWEEN 0 AND 3 " +
            "ORDER BY DATE(RL_CREATE) DESC, TIME(RL_CREATE) DESC, RL_POSITION ASC")
    abstract List<ItemRoll> get();

    public ManagerRoll getManagerRoll() {
        List<ItemRoll> listRollBetween = get(); //TODO Другое имя

        List<String> listCreate = new ArrayList<>();
        List<ItemRollView> listRollView = new ArrayList<>();
        List<ItemRoll> listRoll = new ArrayList<>();

        for (int i = 0; i < listRollBetween.size(); i++) {
            ItemRoll itemRoll = listRollBetween.get(i);
            itemRoll.setExist(true);

            if (!listCreate.contains(itemRoll.getCreate())) {
                listCreate.add(itemRoll.getCreate());

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

        return new ManagerRoll(listCreate, listRollView);
    }

    /**
     * Получение текста для текстовой заметки на основе списка
     *
     * @param rollCreate - Дата создания заметки
     * @return - Строка для текстовой заметки
     */
    public String getText(String rollCreate) {
        List<ItemRoll> listRoll = get(rollCreate);

        StringBuilder rollText = new StringBuilder();
        for (int i = 0; i < listRoll.size(); i++) {
            if (i != 0) rollText.append("\n");
            rollText.append(listRoll.get(i).getText());
        }

        return rollText.toString();
    }

    /**
     * Получение текста для уведомления на основе списка
     *
     * @param rollCreate - Дата создания заметки
     * @param rollCheck  - Количество отмеченых пунктов в заметке
     * @return - Строка для уведомления
     */
    public String getText(String rollCreate, String rollCheck) {
        List<ItemRoll> listRoll = get(rollCreate);

        StringBuilder rollText = new StringBuilder();
        rollText.append(rollCheck).append(" |");

        for (int i = 0; i < listRoll.size(); i++) {
            ItemRoll itemRoll = listRoll.get(i);

            if (itemRoll.isCheck()) rollText.append(" \u2713 ");
            else rollText.append(" - ");

            rollText.append(itemRoll.getText());

            if (i != listRoll.size() - 1) rollText.append(" |");
        }

        return rollText.toString();
    }

    @Query("UPDATE ROLL_TABLE " +
            "SET RL_POSITION = :rollPosition, RL_TEXT = :rollText " +
            "WHERE RL_ID = :rollId")
    public abstract void update(int rollId, int rollPosition, String rollText);

    /**
     * Обновление выполнения конкретного пункта
     *
     * @param rollId    - Id пункта
     * @param rollCheck - Состояние отметки
     */
    @Query("UPDATE ROLL_TABLE " +
            "SET RL_CHECK = :rollCheck " +
            "WHERE RL_ID = :rollId")
    public abstract void update(int rollId, boolean rollCheck);

    /**
     * Обновление выполнения для всех пунктов
     *
     * @param rollCreate - Дата создания заметки
     * @param rollCheck  - Состояние отметки
     */
    @Query("UPDATE ROLL_TABLE " +
            "SET RL_CHECK = :rollCheck " +
            "WHERE RL_CREATE = :rollCreate")
    public abstract void update(String rollCreate, @DefCheck int rollCheck);

    /**
     * Удаление пунктов при сохранении после свайпа
     *
     * @param rollCreate - Дата создания заметки
     * @param rollIdSave - Id, которые остались в заметке
     */
    @Query("DELETE FROM ROLL_TABLE " +
            "WHERE RL_CREATE = :rollCreate AND RL_ID NOT IN (:rollIdSave)")
    public abstract void delete(String rollCreate, List<Integer> rollIdSave);

    public void listAll(TextView textView) {
        List<ItemRoll> listRoll = get();

        String annotation = "Roll Data Base:";
        textView.setText(annotation);

        for (int i = 0; i < listRoll.size(); i++) {
            ItemRoll itemRoll = listRoll.get(i);

            textView.append("\n\n" +
                    "ID: " + itemRoll.getId() + " | " +
                    "CR: " + itemRoll.getCreate() + " | " +
                    "PS: " + itemRoll.getPosition() + " | " +
                    "CH: " + itemRoll.isCheck() + "\n");

            String rollText = itemRoll.getText();
            textView.append("TX: " + rollText.substring(0, Math.min(rollText.length(), 45)).replace("\n", " "));
            if (rollText.length() > 40) textView.append("...");
        }
    }

}
