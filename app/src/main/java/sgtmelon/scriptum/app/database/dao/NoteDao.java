package sgtmelon.scriptum.app.database.dao;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.model.item.StatusItem;
import sgtmelon.scriptum.app.view.fragment.NotesFragment;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.db.BinDef;
import sgtmelon.scriptum.office.conv.BoolConv;

@Dao
@TypeConverters({BoolConv.class})
public abstract class NoteDao extends BaseDao {

    @Insert
    public abstract long insert(NoteItem noteItem);

    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_ID = :id")
    public abstract NoteItem get(long id);

    public NoteModel get(Context context, long id) {
        NoteItem noteItem = get(id);
        List<RollItem> listRoll = getRoll(id);

        StatusItem statusItem = new StatusItem(context, noteItem, false);

        return new NoteModel(noteItem, listRoll, statusItem);
    }

    public List<NoteModel> get(Context context, @BinDef int bin) {
        List<NoteModel> listNoteModel = new ArrayList<>();
        List<NoteItem> listNote = getNote(bin, Help.Pref.getSortNoteOrder(context));

        List<Long> rkVisible = getRankVisible();

        for (int i = 0; i < listNote.size(); i++) {
            NoteItem noteItem = listNote.get(i);
            List<RollItem> listRoll = getRollView(noteItem.getId());
            StatusItem statusItem = new StatusItem(context, noteItem, false);

            NoteModel noteModel = new NoteModel(noteItem, listRoll, statusItem);

            Long[] rkId = noteItem.getRankId();
            if (rkId.length != 0 && !rkVisible.contains(rkId[0])) {
                statusItem.cancelNote();
            } else {
                if (noteItem.isStatus() && NotesFragment.updateStatus) statusItem.notifyNote();

                noteModel.setStatusItem(statusItem);
                listNoteModel.add(noteModel);
            }
        }
        return listNoteModel;
    }

    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_BIN = :bin " +
            "ORDER BY DATE(NT_CREATE) DESC, TIME(NT_CREATE) DESC")
    abstract List<NoteItem> get(@BinDef int bin);

    @Update
    public abstract void update(NoteItem noteItem);

    /**
     * Обновление элементов списка в статус баре
     *
     * @param context - контекст для получения сортировки
     */
    public void update(Context context) {
        List<NoteItem> listNote = getNote(BinDef.out, Help.Pref.getSortNoteOrder(context));
        List<Long> rkVisible = getRankVisible();

        for (int i = 0; i < listNote.size(); i++) {
            NoteItem noteItem = listNote.get(i);
            StatusItem statusItem = new StatusItem(context, noteItem, false);

            Long[] rkId = noteItem.getRankId();
            if (rkId.length != 0 && !rkVisible.contains(rkId[0])) {
                statusItem.cancelNote();
            } else if (noteItem.isStatus()) statusItem.notifyNote();
        }
    }

    /**
     * Обновление положения заметки относительно корзины
     *
     * @param id     - Id обновляемой заметки
     * @param change - Время изменения
     * @param bin    - Положение относительно корзины
     */
    @Query("UPDATE NOTE_TABLE " +
            "SET NT_CHANGE = :change, NT_BIN = :bin " +
            "WHERE NT_ID = :id")
    public abstract void update(long id, String change, boolean bin);

    /**
     * Обновление привязки к статус бару
     *
     * @param id     - Id обновляемой заметки
     * @param status - Привязка к статус бару
     */
    @Query("UPDATE NOTE_TABLE " +
            "SET NT_STATUS = :status " +
            "WHERE NT_ID = :id")
    public abstract void update(long id, boolean status);

    @Delete
    abstract void delete(NoteItem noteItem);

    @Delete
    abstract void delete(List<NoteItem> lisNote);

    public void delete(long id) {
        NoteItem noteItem = get(id);

        Long[] rkId = noteItem.getRankId();
        if (rkId.length != 0) {
            clearRank(noteItem.getId(), rkId);
        }

        delete(noteItem);
    }

    public void clearBin() {
        List<NoteItem> listNote = get(BinDef.in);

        for (int i = 0; i < listNote.size(); i++) {
            NoteItem noteItem = listNote.get(i);

            Long[] rkId = noteItem.getRankId();
            if (rkId.length != 0) {
                clearRank(noteItem.getId(), rkId);
            }
        }

        delete(listNote);
    }

    public void listAll(TextView textView) {
        List<NoteItem> listNote = get(BinDef.in);
        listNote.addAll(get(BinDef.out));

        String annotation = "Note Data Base:";
        textView.setText(annotation);

        for (int i = 0; i < listNote.size(); i++) {
            NoteItem noteItem = listNote.get(i);

            textView.append("\n\n" +
                    "ID: " + noteItem.getId() + " | " +
                    "CR: " + noteItem.getCreate() + " | " +
                    "CH: " + noteItem.getChange() + "\n");

            String name = noteItem.getName();
            if (!name.equals("")) textView.append("NM: " + name + "\n");

            String text = noteItem.getText();
            textView.append("TX: " + text.substring(0, Math.min(text.length(), 45))
                    .replace("\n", " "));
            if (text.length() > 40) textView.append("...");
            textView.append("\n");

            textView.append("CL: " + noteItem.getColor() + " | " +
                    "TP: " + noteItem.getType() + " | " +
                    "BN: " + noteItem.isBin() + "\n" +
                    "RK ID: " + TextUtils.join(", ", noteItem.getRankId()) + " | " +
                    "RK PS: " + TextUtils.join(", ", noteItem.getRankPs()) + "\n" +
                    "ST: " + noteItem.isStatus());
        }
    }

}
