package sgtmelon.scriptum.app.vm;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.model.ModelNote;
import sgtmelon.scriptum.office.annot.def.db.DefBin;

import java.util.List;

public final class NotesViewModel extends AndroidViewModel {

    private List<ModelNote> listModelNote;

    public NotesViewModel(@NonNull Application application) {
        super(application);
    }

    public List<ModelNote> getListModelNote() {
        return listModelNote;
    }

    public void setListModelNote(List<ModelNote> listModelNote) {
        this.listModelNote = listModelNote;
    }

    public List<ModelNote> loadData(@DefBin int bin) {
        Context context = getApplication().getApplicationContext();

        DbRoom db = DbRoom.provideDb(context);
        listModelNote = db.daoNote().get(context, bin);
        db.close();

        return listModelNote;
    }

}
