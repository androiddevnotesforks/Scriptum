package sgtmelon.scriptum.app.screen.note;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.StatusItem;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.key.NoteType;
import sgtmelon.scriptum.office.state.NoteState;
import sgtmelon.scriptum.office.utils.PrefUtils;
import sgtmelon.scriptum.office.utils.TimeUtils;

public final class NoteViewModel extends AndroidViewModel {

    private final Context context;

    private boolean ntCreate;
    private NoteType noteType;
    private long ntId;

    private NoteState noteState;

    private boolean rankEmpty;

    private List<Long> rankVisible;
    private NoteRepo noteRepo;

    private RoomDb db;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void setValue(Bundle bundle) {
        ntCreate = bundle.getBoolean(IntentDef.NOTE_CREATE);
        noteType = NoteType.values()[bundle.getInt(IntentDef.NOTE_TYPE)];
        ntId = bundle.getLong(IntentDef.NOTE_ID);

        if (noteRepo == null) {
            loadData();
        }
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public long getNtId() {
        return ntId;
    }

    public NoteState getNoteState() {
        return noteState;
    }

    public boolean isRankEmpty() {
        return rankEmpty;
    }

    public NoteRepo getNoteRepo() {
        return noteRepo;
    }

    public void setNoteRepo(NoteRepo noteRepo) {
        final NoteItem noteItem = noteRepo.getNoteItem();
        noteRepo.getStatusItem().updateNote(noteItem, rankVisible);

        this.noteRepo = noteRepo;
    }

    private void loadData() {
        db = RoomDb.provideDb(context);

        rankEmpty = db.daoRank().getCount() == 0;
        rankVisible = db.daoRank().getRankVisible();

        if (ntCreate) {
            final String create = TimeUtils.INSTANCE.getTime(context);
            final int color = new PrefUtils(context).getDefaultColor();

            final NoteItem noteItem = new NoteItem(create, color, noteType);
            final StatusItem statusItem = new StatusItem(context, noteItem, false);

            noteRepo = new NoteRepo(noteItem, new ArrayList<>(), statusItem);

            noteState = new NoteState(true);
            noteState.setBin(false);
        } else {
            noteRepo = db.daoNote().get(context, ntId);

            noteState = new NoteState(false);
            noteState.setBin(noteRepo.getNoteItem().isBin());
        }

        db.close();
    }

}