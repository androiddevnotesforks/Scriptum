package sgtmelon.handynotes.db.dao;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.db.converter.ConverterBool;
import sgtmelon.handynotes.db.converter.ConverterInt;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.item.ItemStatus;
import sgtmelon.handynotes.model.manager.ManagerStatus;
import sgtmelon.handynotes.Help;

@Dao
@TypeConverters({ConverterBool.class})
public abstract class DaoNote extends DaoBase {

    @Insert
    public abstract long insertNote(ItemNote itemNote);

    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_ID = :noteId")
    public abstract ItemNote getNote(int noteId);

    @RawQuery
    abstract List<ItemNote> getNote(SupportSQLiteQuery query);

    public List<ItemNote> getNote(int noteBin, String sortKeys) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                "SELECT * FROM " + NT_TB +
                        " WHERE " + NT_BN + " = " + noteBin +
                        " ORDER BY " + sortKeys);

        return getNote(query);
    }

    //TODO: сортировка должна соответствовать той, что в настройках
    @Query("SELECT * FROM NOTE_TABLE WHERE NT_STATUS = 1 " +
            "ORDER BY DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC")
    protected abstract List<ItemNote> getNote();

    /**
     * Конструирует менеджер сообщений с учётом видимых категорий
     *
     * @param context - Используется для создания уведомления
     * @return - Возвращает менеджер сообщений в статус баре
     */
    public ManagerStatus getManagerStatus(Context context) {
        List<ItemNote> listNote = getNote();
        List<String> rankVisible = ConverterInt.fromInteger(getRankVisible());

        List<String> listCreate = new ArrayList<>();
        List<ItemStatus> listStatus = new ArrayList<>();

        for (int i = 0; i < listNote.size(); i++) {
            ItemNote itemNote = listNote.get(i);
            String[] rankId = itemNote.getRankId();

            ItemStatus itemStatus = new ItemStatus(context, itemNote, Help.Array.strListToArr(rankVisible));
            if (rankId.length != 0 && !rankVisible.contains(rankId[0])) {
                itemStatus.cancelNote();
            }

            listCreate.add(itemNote.getCreate());
            listStatus.add(itemStatus);
        }

        return new ManagerStatus(context, listCreate, listStatus);
    }

    @Update
    public abstract void updateNote(ItemNote itemNote);

    /**
     * Обновление положения заметки относительно корзины
     *
     * @param noteId     - Id обновляемой заметки
     * @param noteChange - Время изменения
     * @param noteBin    - Положение относительно корзины
     */
    @Query("UPDATE NOTE_TABLE " +
            "SET NT_CHANGE = :noteChange, NT_BIN = :noteBin " +
            "WHERE NT_ID = :noteId")
    public abstract void updateNote(int noteId, String noteChange, boolean noteBin);

    /**
     * Обновление привязки к статус бару
     *
     * @param noteId     - Id обновляемой заметки
     * @param noteStatus - Привязка к статус бару
     */
    @Query("UPDATE NOTE_TABLE " +
            "SET NT_STATUS = :noteStatus " +
            "WHERE NT_ID = :noteId")
    public abstract void updateNote(int noteId, boolean noteStatus);

    @Delete
    abstract void deleteNote(ItemNote itemNote);

    @Delete
    abstract void deleteNote(List<ItemNote> lisNote);

    public void clearBin() {
        List<ItemNote> listNote = getNote(binTrue, orders[0]);
        List<String> rankVisible = ConverterInt.fromInteger(getRankVisible());

        for (int i = 0; i < listNote.size(); i++) {
            ItemNote itemNote = listNote.get(i);
            String[] rankId = itemNote.getRankId();

            if (rankId.length == 0 || rankVisible.contains(rankId[0])) {
                if (itemNote.getType() == typeRoll) {
                    deleteRoll(itemNote.getCreate());
                }
                if (rankId.length != 0) {
                    clearRank(itemNote.getCreate(), rankId);
                }
            } else listNote.remove(i); //Убираем заметку, которую не надо удалять
        }

        deleteNote(listNote);
    }

    public void deleteNote(int noteId) {
        ItemNote itemNote = getNote(noteId);

        if (itemNote.getType() == typeRoll) {
            deleteRoll(itemNote.getCreate());
        }

        String[] rankId = itemNote.getRankId();
        if (rankId.length != 0) {
            clearRank(itemNote.getCreate(), rankId);
        }

        deleteNote(itemNote);
    }

    public void listAllNote(TextView textView) {
        List<ItemNote> listNote = getNote(binTrue, orders[0]);
        listNote.addAll(getNote(binFalse, orders[0]));

        String annotation = "Note Data Base:";
        textView.setText(annotation);

        for (int i = 0; i < listNote.size(); i++) {
            ItemNote itemNote = listNote.get(i);

            textView.append("\n\n" +
                    "ID: " + itemNote.getId() + " | " +
                    "CR: " + itemNote.getCreate() + " | " +
                    "CH: " + itemNote.getChange() + "\n");

            String noteName = itemNote.getName();
            if (!noteName.equals("")) textView.append("NM: " + noteName + "\n");

            String noteText = itemNote.getText();
            textView.append("TX: " + noteText.substring(0, Math.min(noteText.length(), 45)).replace("\n", " "));
            if (noteText.length() > 40) textView.append("...");
            textView.append("\n");

            textView.append("CL: " + itemNote.getColor() + " | " +
                    "TP: " + itemNote.getType() + " | " +
                    "BN: " + itemNote.isBin() + "\n" +
                    "RK ID: " + TextUtils.join(", ", itemNote.getRankId()) + " | " +
                    "RK PS: " + TextUtils.join(", ", itemNote.getRankPs()) + "\n" +
                    "ST: " + itemNote.isStatus());
        }
    }

}
