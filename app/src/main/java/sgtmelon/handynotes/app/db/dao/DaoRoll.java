package sgtmelon.handynotes.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.office.conv.ConvBool;
import sgtmelon.handynotes.office.annot.def.db.DefCheck;

@Dao
@TypeConverters({ConvBool.class})
public abstract class DaoRoll extends DaoBase {

    @Insert
    public abstract long insert(ItemRoll itemRoll);

    /**
     * Запись пунктов после конвертирования из текстовой заметки
     *
     * @param rollIdNote - Id заметки
     * @param rollText   - Массив потенциальных пунктов
     * @return - Для RepoNote
     */
    public List<ItemRoll> insert(long rollIdNote, String[] rollText) {
        List<ItemRoll> listRoll = new ArrayList<>();

        int pos = 0;
        for (String text : rollText) {
            if (!text.equals("")) {

                ItemRoll itemRoll = new ItemRoll();
                itemRoll.setIdNote(rollIdNote);
                itemRoll.setPosition(pos++);
                itemRoll.setCheck(false);
                itemRoll.setText(text);
                itemRoll.setExist(true);

                long id = insert(itemRoll);
                itemRoll.setId(id);
                listRoll.add(itemRoll);
            }
        }
        return listRoll;
    }

    /**
     * Получение списка всех пунктов с позиции 0 по 3 (4 пунка)
     *
     * @return - Список пунктов
     */
    @Query("SELECT * FROM ROLL_TABLE " +
            "ORDER BY RL_ID_NOTE ASC, RL_POSITION ASC")
    abstract List<ItemRoll> get();

    @Query("SELECT * FROM ROLL_TABLE " +
            "WHERE RL_ID_NOTE = :rollIdNote " +
            "ORDER BY RL_POSITION")
    public abstract List<ItemRoll> get(long rollIdNote);

    /**
     * Получение текста для текстовой заметки на основе списка
     *
     * @param rollIdNote - Id заметки
     * @return - Строка для текстовой заметки
     */
    public String getText(long rollIdNote) {
        List<ItemRoll> listRoll = get(rollIdNote);

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
     * @param noteId - Id заметки
     * @param rollCheck  - Количество отмеченых пунктов в заметке
     * @return - Строка для уведомления
     */
    public String getText(long noteId, String rollCheck) {
        List<ItemRoll> listRoll = get(noteId);

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
    public abstract void update(long rollId, int rollPosition, String rollText);

    /**
     * Обновление выполнения конкретного пункта
     *
     * @param rollId    - Id пункта
     * @param rollCheck - Состояние отметки
     */
    @Query("UPDATE ROLL_TABLE " +
            "SET RL_CHECK = :rollCheck " +
            "WHERE RL_ID = :rollId")
    public abstract void update(long rollId, boolean rollCheck);

    /**
     * Обновление выполнения для всех пунктов
     *
     * @param rollIdNote - Id заметки
     * @param rollCheck  - Состояние отметки
     */
    @Query("UPDATE ROLL_TABLE " +
            "SET RL_CHECK = :rollCheck " +
            "WHERE RL_ID_NOTE = :rollIdNote")
    public abstract void update(long rollIdNote, @DefCheck int rollCheck);

    /**
     * Удаление пунктов при сохранении после свайпа
     *
     * @param rollIdNote - Id заметки
     * @param rollIdSave - Id, которые остались в заметке
     */
    @Query("DELETE FROM ROLL_TABLE " +
            "WHERE RL_ID_NOTE = :rollIdNote AND RL_ID NOT IN (:rollIdSave)")
    public abstract void delete(long rollIdNote, List<Long> rollIdSave);

    public void listAll(TextView textView) {
        List<ItemRoll> listRoll = get();

        String annotation = "Roll Data Base:";
        textView.setText(annotation);

        for (int i = 0; i < listRoll.size(); i++) {
            ItemRoll itemRoll = listRoll.get(i);

            textView.append("\n\n" +
                    "ID: " + itemRoll.getId() + " | " +
                    "ID_NT: " + itemRoll.getIdNote() + " | " +
                    "PS: " + itemRoll.getPosition() + " | " +
                    "CH: " + itemRoll.isCheck() + "\n");

            String rollText = itemRoll.getText();
            textView.append("TX: " + rollText.substring(0, Math.min(rollText.length(), 45)).replace("\n", " "));
            if (rollText.length() > 40) textView.append("...");
        }
    }

}
