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
import sgtmelon.handynotes.db.converter.ConverterList;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.item.ItemStatus;
import sgtmelon.handynotes.model.manager.ManagerStatus;
import sgtmelon.handynotes.Help;

@Dao
@TypeConverters({ConverterBool.class})
public abstract class DaoNote extends DaoBase {

    @Insert
    public abstract long insert(ItemNote itemNote);

    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_ID = :noteId")
    public abstract ItemNote get(int noteId);

    @RawQuery
    abstract List<ItemNote> get(SupportSQLiteQuery query);

    public List<ItemNote> get(int noteBin, String sortKeys) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                "SELECT * FROM " + NT_TB +
                        " WHERE " + NT_BN + " = " + noteBin +
                        " ORDER BY " + sortKeys);

        return get(query);
    }

    /**
     * Конструирует менеджер сообщений с учётом видимых категорий
     *
     * @param context - Используется для создания уведомления
     * @return - Возвращает менеджер сообщений в статус баре
     */
    public ManagerStatus getManagerStatus(Context context) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                "SELECT * FROM " + NT_TB +
                        " WHERE " + NT_ST + " = " + 1 +
                        " ORDER BY " + Help.Pref.getSortNoteOrder(context));

        List<ItemNote> listNote = get(query);
        List<String> rankVisible = ConverterInt.fromInteger(getRankVisible());
        String[] rankVs = ConverterList.fromList(rankVisible);

        List<String> listCreate = new ArrayList<>();
        List<ItemStatus> listStatus = new ArrayList<>();

        for (int i = listNote.size() - 1; i >= 0 ; i--) {
            ItemNote itemNote = listNote.get(i);
            String[] rankId = itemNote.getRankId();

            ItemStatus itemStatus = new ItemStatus(context, itemNote, rankVs);
            if (rankId.length != 0 && !rankVisible.contains(rankId[0])) {
                itemStatus.cancelNote();
            }

            listCreate.add(itemNote.getCreate());
            listStatus.add(itemStatus);
        }

        return new ManagerStatus(context, listCreate, listStatus);
    }

    @Update
    public abstract void update(ItemNote itemNote);

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
    public abstract void update(int noteId, String noteChange, boolean noteBin);

    /**
     * Обновление привязки к статус бару
     *
     * @param noteId     - Id обновляемой заметки
     * @param noteStatus - Привязка к статус бару
     */
    @Query("UPDATE NOTE_TABLE " +
            "SET NT_STATUS = :noteStatus " +
            "WHERE NT_ID = :noteId")
    public abstract void update(int noteId, boolean noteStatus);

    @Delete
    abstract void delete(ItemNote itemNote);

    @Delete
    abstract void delete(List<ItemNote> lisNote);

    public void delete(int noteId) {
        ItemNote itemNote = get(noteId);

        if (itemNote.getType() == typeRoll) {
            deleteRoll(itemNote.getCreate());
        }

        String[] rankId = itemNote.getRankId();
        if (rankId.length != 0) {
            clearRank(itemNote.getCreate(), rankId);
        }

        delete(itemNote);
    }

    public void clearBin() {
        List<ItemNote> listNote = get(binTrue, orders[0]);
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

        delete(listNote);
    }

    public void listAll(TextView textView) {
        List<ItemNote> listNote = get(binTrue, orders[0]);
        listNote.addAll(get(binFalse, orders[0]));

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
