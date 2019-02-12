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
import androidx.room.Update;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.model.item.StatusItem;
import sgtmelon.scriptum.app.view.fragment.main.NotesFragment;
import sgtmelon.scriptum.office.annot.def.BinDef;
import sgtmelon.scriptum.office.utils.PrefUtils;

/**
 * Класс для общения Dao заметок {@link RoomDb}
 */
@Dao
public abstract class NoteDao extends BaseDao {

    @Insert
    public abstract long insert(NoteItem noteItem);

    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_ID = :id")
    public abstract NoteItem get(long id);

    public NoteRepo get(Context context, long id) {
        final NoteItem noteItem = get(id);
        final List<RollItem> listRoll = getRoll(id);

        final StatusItem statusItem = new StatusItem(context, noteItem, false);

        return new NoteRepo(noteItem, listRoll, statusItem);
    }

    public List<NoteRepo> get(Context context, @BinDef int bin) {
        final List<NoteRepo> listNoteRepo = new ArrayList<>();
        final List<NoteItem> listNote = getNote(bin, new PrefUtils(context).getSortNoteOrder());

        final List<Long> rkVisible = getRankVisible();

        for (int i = 0; i < listNote.size(); i++) {
            final NoteItem noteItem = listNote.get(i);
            final List<RollItem> listRoll = getRollView(noteItem.getId());
            final StatusItem statusItem = new StatusItem(context, noteItem, false);

            final NoteRepo noteRepo = new NoteRepo(noteItem, listRoll, statusItem);

            final List<Long> rkId = noteItem.getRankId();
            if (rkId.size() != 0 && !rkVisible.contains(rkId.get(0))) {
                statusItem.cancelNote();
            } else {
                if (noteItem.isStatus() && NotesFragment.Companion.getUpdateStatus()) {
                    statusItem.notifyNote();
                }

                noteRepo.setStatusItem(statusItem);
                listNoteRepo.add(noteRepo);
            }
        }
        return listNoteRepo;
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
     * @param context - Контекст для получения сортировки
     */
    public void update(Context context) {
        final List<NoteItem> listNote = getNote(BinDef.out, new PrefUtils(context).getSortNoteOrder());
        final List<Long> rkVisible = getRankVisible();

        for (int i = 0; i < listNote.size(); i++) {
            final NoteItem noteItem = listNote.get(i);
            final StatusItem statusItem = new StatusItem(context, noteItem, false);

            final List<Long> rkId = noteItem.getRankId();
            if (rkId.size() != 0 && !rkVisible.contains(rkId.get(0))) {
                statusItem.cancelNote();
            } else if (noteItem.isStatus()) {
                statusItem.notifyNote();
            }
        }
    }

    @Query("UPDATE NOTE_TABLE " +
            "SET NT_CHANGE = :change, NT_BIN = :bin " +
            "WHERE NT_ID = :id")
    public abstract void update(long id, String change, boolean bin);

    @Query("UPDATE NOTE_TABLE " +
            "SET NT_STATUS = :status " +
            "WHERE NT_ID = :id")
    public abstract void update(long id, boolean status);

    @Delete
    abstract void delete(NoteItem noteItem);

    @Delete
    abstract void delete(List<NoteItem> lisNote);

    /**
     * Удаление заметки с чисткой категории
     *
     * @param id - Идентификатор заметки
     */
    public void delete(long id) {
        final NoteItem noteItem = get(id);

        final List<Long> rkId = noteItem.getRankId();
        if (rkId.size() != 0) {
            clearRank(noteItem.getId(), rkId);
        }

        delete(noteItem);
    }

    public void clearBin() {
        final List<NoteItem> listNote = get(BinDef.in);

        for (int i = 0; i < listNote.size(); i++) {
            final NoteItem noteItem = listNote.get(i);

            final List<Long> rkId = noteItem.getRankId();
            if (rkId.size() != 0) {
                clearRank(noteItem.getId(), rkId);
            }
        }

        delete(listNote);
    }

    public void listAll(TextView textView) {
        final List<NoteItem> listNote = get(BinDef.in);
        listNote.addAll(get(BinDef.out));

        final String annotation = "Note Data Base:";
        textView.setText(annotation);

        for (int i = 0; i < listNote.size(); i++) {
            final NoteItem noteItem = listNote.get(i);

            textView.append("\n\n" +
                    "ID: " + noteItem.getId() + " | " +
                    "CR: " + noteItem.getCreate() + " | " +
                    "CH: " + noteItem.getChange() + "\n");

            final String name = noteItem.getName();
            if (!TextUtils.isEmpty(name)) {
                textView.append("NM: " + name + "\n");
            }

            final String text = noteItem.getText();
            textView.append("TX: " + text.substring(0, Math.min(text.length(), 45))
                    .replace("\n", " "));

            if (text.length() > 40) {
                textView.append("...");
            }

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