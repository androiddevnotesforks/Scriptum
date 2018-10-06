package sgtmelon.scriptum.app.database.dao;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.office.annot.def.db.CheckDef;
import sgtmelon.scriptum.office.conv.BoolConv;

@Dao
@TypeConverters({BoolConv.class})
public abstract class RollDao extends BaseDao {

    @Insert
    public abstract long insert(RollItem rollItem);

    /**
     * Запись пунктов после конвертирования из текстовой заметки
     *
     * @param idNote - Id заметки
     * @param text   - Массив потенциальных пунктов
     * @return - Для NoteModel
     */
    public List<RollItem> insert(long idNote, String[] text) {
        List<RollItem> listRoll = new ArrayList<>();

        int p = 0;
        for (String aText : text) {
            if (!aText.equals("")) {
                RollItem rollItem = new RollItem();
                rollItem.setIdNote(idNote);
                rollItem.setPosition(p++);
                rollItem.setCheck(false);
                rollItem.setText(aText);
                rollItem.setExist(true);

                long id = insert(rollItem);
                rollItem.setId(id);

                listRoll.add(rollItem);
            }
        }

        return listRoll;
    }

    @Query("SELECT * FROM ROLL_TABLE " +
            "ORDER BY RL_ID_NOTE ASC, RL_POSITION ASC")
    abstract List<RollItem> get();

    /**
     * Получение текста для текстовой заметки на основе списка
     *
     * @param idNote - Id заметки
     * @return - Строка для текстовой заметки
     */
    public String getText(long idNote) {
        List<RollItem> listRoll = getRoll(idNote);

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
        List<RollItem> listRoll = getRoll(idNote);

        StringBuilder text = new StringBuilder();
        text.append(check).append(" |");

        for (int i = 0; i < listRoll.size(); i++) {
            RollItem rollItem = listRoll.get(i);

            if (rollItem.isCheck()) text.append(" \u2713 ");
            else text.append(" - ");

            text.append(rollItem.getText());

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
    public abstract void update(long idNote, @CheckDef int check);

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
        List<RollItem> listRoll = get();

        String annotation = "Roll Data Base:";
        textView.setText(annotation);

        for (int i = 0; i < listRoll.size(); i++) {
            RollItem rollItem = listRoll.get(i);

            textView.append("\n\n" +
                    "ID: " + rollItem.getId() + " | " +
                    "ID_NT: " + rollItem.getIdNote() + " | " +
                    "PS: " + rollItem.getPosition() + " | " +
                    "CH: " + rollItem.isCheck() + "\n");

            String text = rollItem.getText();
            textView.append("TX: " + text.substring(0, Math.min(text.length(), 45))
                    .replace("\n", " "));
            if (text.length() > 40) textView.append("...");
        }
    }

}
