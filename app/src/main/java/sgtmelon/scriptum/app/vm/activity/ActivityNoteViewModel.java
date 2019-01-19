package sgtmelon.scriptum.app.vm.activity;

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
import sgtmelon.scriptum.app.vm.fragment.FragmentNoteViewModel;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.st.NoteSt;
import sgtmelon.scriptum.office.utils.PrefUtils;
import sgtmelon.scriptum.office.utils.TimeUtils;

public final class ActivityNoteViewModel extends AndroidViewModel implements
        MenuIntf.Note.DeleteMenuClick {

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

        if (noteRepo == null) {
            loadData();
        }
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
            final String create = TimeUtils.getTime(context);
            final int color = PrefUtils.getInstance(context).getDefaultColor();

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

    @ColorDef
    public int resetFragmentData(long id, @NonNull FragmentNoteViewModel viewModel) {
        db = RoomDb.provideDb(context);
        noteRepo = db.daoNote().get(context, id);
        db.close();

        viewModel.setNoteRepo(noteRepo);

        return noteRepo.getNoteItem().getColor();
    }

    @Override
    public void onMenuRestoreClick() {
        db = RoomDb.provideDb(context);
        db.daoNote().update(
                noteRepo.getNoteItem().getId(), TimeUtils.getTime(context), false
        );
        db.close();
    }

    @Override
    public void onMenuRestoreOpenClick() {
        noteSt.setBin(false);

        final NoteItem noteItem = noteRepo.getNoteItem();
        noteItem.setChange(TimeUtils.getTime(context));
        noteItem.setBin(false);

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem);
        db.close();
    }

    @Override
    public void onMenuClearClick() {
        db = RoomDb.provideDb(context);
        db.daoNote().delete(noteRepo.getNoteItem().getId());
        db.close();

        noteRepo.update(false);
    }

    @Override
    public void onMenuDeleteClick() {
        final NoteItem noteItem = noteRepo.getNoteItem();

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem.getId(), TimeUtils.getTime(context), true);
        if (noteItem.isStatus()) {
            db.daoNote().update(noteItem.getId(), false);
        }
        db.close();

        noteRepo.update(false);
    }

}