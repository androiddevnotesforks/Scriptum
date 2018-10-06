package sgtmelon.scriptum.app.model;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.model.item.StatusItem;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.annot.def.db.CheckDef;

public final class NoteModel { // TODO: 02.10.2018 чистая модель

    @Embedded
    private NoteItem noteItem;

    @Relation(parentColumn = DbAnn.NT_ID, entityColumn = DbAnn.RL_ID_NT)
    private List<RollItem> listRoll;

    @Ignore
    private StatusItem statusItem;

    public NoteModel(NoteItem noteItem, List<RollItem> listRoll, StatusItem statusItem) {
        this.noteItem = noteItem;
        this.listRoll = listRoll;
        this.statusItem = statusItem;
    }

    public NoteItem getNoteItem() {
        return noteItem;
    }

    public void setNoteItem(NoteItem noteItem) {
        this.noteItem = noteItem;
    }

    public List<RollItem> getListRoll() {
        return listRoll;
    }

    public void setListRoll(List<RollItem> listRoll) {
        this.listRoll = listRoll;
    }

    /**
     * Сброс списка при конвертированиия Список -> Текст
     */
    public void setListRoll() {
        listRoll = new ArrayList<>();
    }

    public void setStatusItem(StatusItem statusItem) {
        this.statusItem = statusItem;
    }

    /**
     * При отметке всех пунктов
     */
    public void updateListRoll(@CheckDef int rollCheck) {
        for (int i = 0; i < listRoll.size(); i++) {
            RollItem rollItem = listRoll.get(i);
            rollItem.setCheck(rollCheck == CheckDef.done);
            listRoll.set(i, rollItem);
        }
    }

    public void updateItemStatus(boolean noteStatus) {
        if (noteStatus) statusItem.notifyNote();
        else statusItem.cancelNote();
    }

    public void updateItemStatus() {
        statusItem.updateNote(noteItem, true);
    }

    public void updateItemStatus(List<Long> rankVisible) {
        statusItem.updateNote(noteItem, rankVisible);
    }

}
