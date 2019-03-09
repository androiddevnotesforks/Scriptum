package sgtmelon.scriptum.app.room.dao;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.room.RoomDb;

/**
 * Класс для общения Dao списка {@link RoomDb}
 */
@Dao
public abstract class RollDao extends BaseDao {

    @Insert
    public abstract long insert(RollItem rollItem);

    /**
     * Запись пунктов после конвертирования из текстовой заметки
     *
     * @param noteId - Id заметки
     * @param text   - Текст потенциальных пунктов
     * @return - Список для {@link NoteModel}
     */
    public List<RollItem> insert(long noteId, String text) {
        final String[] textToRoll = text.split("\n");
        final List<RollItem> listRoll = new ArrayList<>();

        int p = 0;
        for (String toRoll : textToRoll) {
            if (TextUtils.isEmpty(toRoll)) continue;

            final RollItem rollItem = new RollItem();
            rollItem.setNoteId(noteId);
            rollItem.setPosition(p++);
            rollItem.setCheck(false);
            rollItem.setText(toRoll);

            rollItem.setId(insert(rollItem));

            listRoll.add(rollItem);
        }

        return listRoll;
    }

    @Query("SELECT * FROM ROLL_TABLE " +
            "ORDER BY RL_NOTE_ID ASC, RL_POSITION ASC")
    abstract List<RollItem> get();

    /**
     * Получение текста для текстовой заметки на основе списка
     *
     * @param idNote - Id заметки
     * @return - Строка для текстовой заметки
     */
    public String getText(long idNote) {
        final List<RollItem> listRoll = getRoll(idNote);

        final StringBuilder text = new StringBuilder();
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
        final List<RollItem> listRoll = getRoll(idNote);

        final StringBuilder text = new StringBuilder();
        text.append(check).append(" |");

        for (int i = 0; i < listRoll.size(); i++) {
            final RollItem rollItem = listRoll.get(i);

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
     * @param id - Id заметки
     * @param check  - Состояние отметки
     */
    @Query("UPDATE ROLL_TABLE " +
            "SET RL_CHECK = :check " +
            "WHERE RL_NOTE_ID = :id")
    public abstract void updateAllCheck(long id, boolean check);

    /**
     * Удаление пунктов при сохранении после свайпа
     *
     * @param idNote - Id заметки
     * @param idSave - Id, которые остались в заметке
     */
    @Query("DELETE FROM ROLL_TABLE " +
            "WHERE RL_NOTE_ID = :idNote AND RL_ID NOT IN (:idSave)")
    public abstract void delete(long idNote, List<Long> idSave);

    /**
     * @param idNote - Id удаляемой заметки
     */
    @Query("DELETE FROM ROLL_TABLE " +
            "WHERE RL_NOTE_ID = :idNote")
    public abstract void delete(long idNote);

    public void listAll(TextView textView) {
        final List<RollItem> listRoll = get();

        final String annotation = "Roll Data Base:";
        textView.setText(annotation);

        for (int i = 0; i < listRoll.size(); i++) {
            final RollItem rollItem = listRoll.get(i);

            textView.append("\n\n" +
                    "ID: " + rollItem.getId() + " | " +
                    "ID_NT: " + rollItem.getNoteId() + " | " +
                    "PS: " + rollItem.getPosition() + " | " +
                    "CH: " + rollItem.isCheck() + "\n");

            final String text = rollItem.getText();
            textView.append("TX: " + text.substring(0, Math.min(text.length(), 45))
                    .replace("\n", " "));
            if (text.length() > 40) textView.append("...");
        }
    }

}