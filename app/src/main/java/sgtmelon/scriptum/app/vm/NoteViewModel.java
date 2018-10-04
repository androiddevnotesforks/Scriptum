package sgtmelon.scriptum.app.vm;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.model.ModelNote;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.item.ItemStatus;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.st.StNote;

public final class NoteViewModel extends AndroidViewModel {

    private final Context context;

    private StNote stNote;

    private boolean ntCreate;
    private int ntType;
    private long ntId;

    private List<Long> rankVisible;
    private ModelNote modelNote;

    private DbRoom db;

    public NoteViewModel(@NonNull Application application) {
        super(application);

        context = application.getApplicationContext();
    }

    public void setValue(Bundle bundle) {
        ntCreate = bundle.getBoolean(DefIntent.NOTE_CREATE);
        ntType = bundle.getInt(DefIntent.NOTE_TYPE);
        ntId = bundle.getLong(DefIntent.NOTE_ID);

        if (modelNote == null) loadData();
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

    public ModelNote getModelNote() {
        return modelNote;
    }

    public void setModelNote(ModelNote modelNote) {
        this.modelNote = modelNote;
        modelNote.updateItemStatus(rankVisible);
    }

    public void setRepoNote(boolean status) {
        modelNote.updateItemStatus(status);
    }

    private void loadData() {
        db = DbRoom.provideDb(context);
        rankVisible = db.daoRank().getRankVisible();
        if (ntCreate) {
            ItemNote itemNote = new ItemNote(context, ntType);
            ItemStatus itemStatus = new ItemStatus(context, itemNote, false);

            modelNote = new ModelNote(itemNote, new ArrayList<>(), itemStatus);

            stNote = new StNote(true);
            stNote.setBin(false);
        } else {
            modelNote = db.daoNote().get(context, ntId);

            stNote = new StNote(false);
            stNote.setBin(modelNote.getItemNote().isBin());
        }
        db.close();
    }

    public ModelNote loadData(long id) {
        db = DbRoom.provideDb(context);
        modelNote = db.daoNote().get(context, id);
        db.close();

        return modelNote;
    }

}
