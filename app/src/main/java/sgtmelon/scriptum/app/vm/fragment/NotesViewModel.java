package sgtmelon.scriptum.app.vm.fragment;

import android.app.Application;
import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.office.annot.def.StateDef;

public final class NotesViewModel extends AndroidViewModel {

    private List<NoteRepo> listModel;

    public NotesViewModel(@NonNull Application application) {
        super(application);
    }

    public List<NoteRepo> getListModel() {
        return listModel;
    }

    public void setListModel(List<NoteRepo> listModel) {
        this.listModel = listModel;
    }

    public List<NoteRepo> loadData(@StateDef.Bin int bin) {
        Context context = getApplication().getApplicationContext();

        RoomDb db = RoomDb.provideDb(context);
        listModel = db.daoNote().get(context, bin);
        db.close();

        return listModel;
    }

}
