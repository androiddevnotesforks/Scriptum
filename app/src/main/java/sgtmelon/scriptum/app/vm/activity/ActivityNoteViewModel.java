package sgtmelon.scriptum.app.vm.activity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.StatusItem;
import sgtmelon.scriptum.office.HelpUtils;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.st.NoteSt;
import sgtmelon.scriptum.office.utils.PrefUtils;

public final class ActivityNoteViewModel extends AndroidViewModel {

    private final Context context;

    private boolean ntCreate;
    private int ntType;
    private long ntId;

    private NoteSt noteSt;

    private boolean rankEmpty;

    private List<Long> rankVisible;
    private NoteRepo noteRepo;

    private RoomDb db;

    public ActivityNoteViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void setValue(Bundle bundle) {
        ntCreate = bundle.getBoolean(IntentDef.NOTE_CREATE);
        ntType = bundle.getInt(IntentDef.NOTE_TYPE);
        ntId = bundle.getLong(IntentDef.NOTE_ID);

        if (noteRepo == null) loadData();
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

    public boolean isRankEmpty() {
        return rankEmpty;
    }

    public NoteRepo getNoteRepo() {
        return noteRepo;
    }

    public void setNoteRepo(NoteRepo noteRepo) {
        noteRepo.update(rankVisible);

        this.noteRepo = noteRepo;
    }

    public void setRepoNote(boolean status) {
        noteRepo.update(status);
    }

    private void loadData() {
        db = RoomDb.provideDb(context);

        rankEmpty = db.daoRank().getCount() == 0;
        rankVisible = db.daoRank().getRankVisible();

        if (ntCreate) {
            final String create = HelpUtils.Time.getCurrentTime(context);

            final SharedPreferences pref = PrefUtils.getInstance(context);
            final int color = pref.getInt(context.getString(R.string.pref_key_color), context.getResources().getInteger(R.integer.pref_color_default));

            final NoteItem noteItem = new NoteItem(create, color, ntType);
            final StatusItem statusItem = new StatusItem(context, noteItem, false);

            noteRepo = new NoteRepo(noteItem, new ArrayList<>(), statusItem);

            noteSt = new NoteSt(true);
            noteSt.setBin(false);
        } else {
            noteRepo = db.daoNote().get(context, ntId);

            noteSt = new NoteSt(false);
            noteSt.setBin(noteRepo.getNoteItem().isBin());
        }

        db.close();
    }

    public NoteRepo loadData(long id) {
        db = RoomDb.provideDb(context);
        noteRepo = db.daoNote().get(context, id);
        db.close();

        return noteRepo;
    }

}
