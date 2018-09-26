package sgtmelon.scriptum.app.database.dao;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import sgtmelon.scriptum.app.model.item.ItemRoll;
import sgtmelon.scriptum.office.annot.def.db.DefCheck;
import sgtmelon.scriptum.office.conv.ConvBool;

@Dao
@TypeConverters({ConvBool.class})
public abstract class DaoRoll extends DaoBase {

    @Insert
    public abstract long insert(ItemRoll itemRoll);

    /**
     * Запись пунктов после конвертирования из текстовой заметки
     *
     * @param idNote - Id заметки
     * @param text   - Массив потенциальных пунктов
     * @return - Для RepoNote
     */
    public List<ItemRoll> insert(long idNote, String[] text) {
        List<ItemRoll> listRoll = new ArrayList<>();

        int p = 0;
        for (String aText : text) {
            if (!aText.equals("")) {
                ItemRoll itemRoll = new ItemRoll();
                itemRoll.setIdNote(idNote);
                itemRoll.setPosition(p++);
                itemRoll.setCheck(false);
                itemRoll.setText(aText);
                itemRoll.setExist(true);

                long id = insert(itemRoll);
                itemRoll.setId(id);

                listRoll.add(itemRoll);
            }
        }

        return listRoll;
    }

    @Query("SELECT * FROM ROLL_TABLE " +
            "ORDER BY RL_ID_NOTE ASC, RL_POSITION ASC")
    abstract List<ItemRoll> get();

    /**
     * Получение текста для текстовой заметки на основе списка
     *
     * @param idNote - Id заметки
     * @return - Строка для текстовой заметки
     */
    public String getText(long idNote) {
        List<ItemRoll> listRoll = getRoll(idNote);

        StringBuilder text = new StringBuilder();
        for (int i = 0; i < listRoll.size(); i++) {
            if (i != 0) text.append("\n");
            text.append(listRoll.get(i).getText());
        }

        return text.toString();
    }

    /**
     * Получение текста для уведомления на основе списка
     *
     * @param idNote - Id заметки
     * @param check  - Количество отмеченых пунктов в заметке
     * @return - Строка для уведомления
     */
    public String getText(long idNote, String check) {
        List<ItemRoll> listRoll = getRoll(idNote);

        StringBuilder text = new StringBuilder();
        text.append(check).append(" |");

        for (int i = 0; i < listRoll.size(); i++) {
            ItemRoll itemRoll = listRoll.get(i);

            if (itemRoll.isCheck()) text.append(" \u2713 ");
            else text.append(" - ");

            text.append(itemRoll.getText());

            if (i != listRoll.size() - 1) text.append(" |");
        }

        return text.toString();
    }

    @Query("UPDATE ROLL_TABLE " +
            "SET RL_POSITION = :position, RL_TEXT = :text " +
            "WHERE RL_ID = :id")
    public abstract void update(long id, int position, String text);

    /**
     * Обновление выполнения конкретного пункта
     *
     * @param id    - Id пункта
     * @param check - Состояние отметки
     */
    @Query("UPDATE ROLL_TABLE " +
            "SET RL_CHECK = :check " +
            "WHERE RL_ID = :id")
    public abstract void update(long id, boolean check);

    /**
     * Обновление выполнения для всех пунктов
     *
     * @param idNote - Id заметки
     * @param check  - Состояние отметки
     */
    @Query("UPDATE ROLL_TABLE " +
            "SET RL_CHECK = :check " +
            "WHERE RL_ID_NOTE = :idNote")
    public abstract void update(long idNote, @DefCheck int check);

    /**
     * Удаление пунктов при сохранении после свайпа
     *
     * @param idNote - Id заметки
     * @param idSave - Id, которые остались в заметке
     */
    @Query("DELETE FROM ROLL_TABLE " +
            "WHERE RL_ID_NOTE = :idNote AND RL_ID NOT IN (:idSave)")
    public abstract void delete(long idNote, List<Long> idSave);

    /**
     * @param idNote - Id удаляемой заметки
     */
    @Query("DELETE FROM ROLL_TABLE " +
            "WHERE RL_ID_NOTE = :idNote")
    public abstract void delete(long idNote);

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

            String text = itemRoll.getText();
            textView.append("TX: " + text.substring(0, Math.min(text.length(), 45)).replace("\n", " "));
            if (text.length() > 40) textView.append("...");
        }
    }

}
