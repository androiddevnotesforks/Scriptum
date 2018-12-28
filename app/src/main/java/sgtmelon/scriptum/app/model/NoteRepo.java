package sgtmelon.scriptum.app.model;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.model.item.StatusItem;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.annot.def.CheckDef;

/**
 * Репозиторий заметки
 */
public final class NoteRepo {

    @Embedded private NoteItem noteItem;

    @Relation(parentColumn = DbAnn.Note.ID, entityColumn = DbAnn.Roll.NOTE_ID)
    private List<RollItem> listRoll;

    @Ignore private StatusItem statusItem;

    public NoteRepo(NoteItem noteItem, List<RollItem> listRoll, StatusItem statusItem) {
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

    public StatusItem getStatusItem() {
        return statusItem;
    }

    public void setStatusItem(StatusItem statusItem) {
        this.statusItem = statusItem;
    }

    /**
     * При отметке всех пунктов
     */
    public void update(@CheckDef int rollCheck) {
        for (RollItem rollItem : listRoll) {
            rollItem.setCheck(rollCheck == CheckDef.done);
        }
    }

    public void update(boolean status) {
        if (status) {
            statusItem.notifyNote();
        } else {
            statusItem.cancelNote();
        }
    }

}