package sgtmelon.scriptum.app.vm;

import android.app.Application;
import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.office.annot.def.db.BinDef;

public final class NotesViewModel extends AndroidViewModel {

    private List<NoteModel> listModel;

    public NotesViewModel(@NonNull Application application) {
        super(application);
    }

    public List<NoteModel> getListModel() {
        return listModel;
    }

    public void setListModel(List<NoteModel> listModel) {
        this.listModel = listModel;
    }

    public List<NoteModel> loadData(@BinDef int bin) {
        Context context = getApplication().getApplicationContext();

        RoomDb db = RoomDb.provideDb(context);
        listModel = db.daoNote().get(context, bin);
        db.close();

        return listModel;
    }

}
