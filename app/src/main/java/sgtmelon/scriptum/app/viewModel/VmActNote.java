package sgtmelon.scriptum.app.viewModel;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.item.ItemStatus;
import sgtmelon.scriptum.app.model.repo.RepoNote;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.st.StNote;

public class VmActNote extends AndroidViewModel {

    private final Context context;

    private StNote stNote;
    private int ntType;
    private long ntId;

    private List<Long> rankVisible;
    private RepoNote repoNote;

    private DbRoom db;

    public VmActNote(@NonNull Application application) {
        super(application);

        context = application.getApplicationContext();
    }

    public void setValue(Bundle bundle) {
        stNote = bundle.getParcelable(DefIntent.STATE_NOTE);
        ntType = bundle.getInt(DefIntent.NOTE_TYPE);
        ntId = bundle.getLong(DefIntent.NOTE_ID);

        if (repoNote == null) loadData();
    }

    public int getNtType() {
        return ntType;
    }

    public long getNtId() {
        return ntId;
    }

    public StNote getStNote() {
        return stNote;
    }

    public void setStNote(StNote stNote) {
        this.stNote = stNote;
    }

    public RepoNote getRepoNote() {
        return repoNote;
    }

    public void setRepoNote(RepoNote repoNote) {
        this.repoNote = repoNote;
        repoNote.updateItemStatus(rankVisible);
    }

    public void setRepoNote(boolean status) {
        repoNote.updateItemStatus(status);
    }

    private void loadData() {
        db = DbRoom.provideDb(context);
        rankVisible = db.daoRank().getRankVisible();
        if (stNote.isCreate()) {
            ItemNote itemNote = new ItemNote(context, ntType);
            ItemStatus itemStatus = new ItemStatus(context, itemNote, false);

            repoNote = new RepoNote(itemNote, new ArrayList<>(), itemStatus);
        } else {
            repoNote = db.daoNote().get(context, ntId);
        }
        db.close();
    }

    public RepoNote loadData(long id) {
        db = DbRoom.provideDb(context);
        repoNote = db.daoNote().get(context, id);
        db.close();

        return repoNote;
    }

}
