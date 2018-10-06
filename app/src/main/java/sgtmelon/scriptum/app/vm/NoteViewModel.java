package sgtmelon.scriptum.app.vm;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.StatusItem;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.st.NoteSt;

public final class NoteViewModel extends AndroidViewModel {

    private final Context context;

    private NoteSt noteSt;

    private boolean ntCreate;
    private int ntType;
    private long ntId;

    private List<Long> rankVisible;
    private NoteModel noteModel;

    private RoomDb db;

    public NoteViewModel(@NonNull Application application) {
        super(application);

        context = application.getApplicationContext();
    }

    public void setValue(Bundle bundle) {
        ntCreate = bundle.getBoolean(IntentDef.NOTE_CREATE);
        ntType = bundle.getInt(IntentDef.NOTE_TYPE);
        ntId = bundle.getLong(IntentDef.NOTE_ID);

        if (noteModel == null) loadData();
    }

    public int getNtType() {
        return ntType;
    }

    public long getNtId() {
        return ntId;
    }

    public NoteSt getNoteSt() {
        return noteSt;
    }

    public void setNoteSt(NoteSt noteSt) {
        this.noteSt = noteSt;
    }

    public NoteModel getNoteModel() {
        return noteModel;
    }

    public void setNoteModel(NoteModel noteModel) {
        this.noteModel = noteModel;
        noteModel.updateItemStatus(rankVisible);
    }

    public void setRepoNote(boolean status) {
        noteModel.updateItemStatus(status);
    }

    private void loadData() {
        db = RoomDb.provideDb(context);
        rankVisible = db.daoRank().getRankVisible();
        if (ntCreate) {
            NoteItem noteItem = new NoteItem(context, ntType);
            StatusItem statusItem = new StatusItem(context, noteItem, false);

            noteModel = new NoteModel(noteItem, new ArrayList<>(), statusItem);

            noteSt = new NoteSt(true);
            noteSt.setBin(false);
        } else {
            noteModel = db.daoNote().get(context, ntId);

            noteSt = new NoteSt(false);
            noteSt.setBin(noteModel.getNoteItem().isBin());
        }
        db.close();
    }

    public NoteModel loadData(long id) {
        db = RoomDb.provideDb(context);
        noteModel = db.daoNote().get(context, id);
        db.close();

        return noteModel;
    }

}
