package sgtmelon.scriptum.app.vm.fragment;

import android.app.Application;
import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.view.fragment.BinFragment;
import sgtmelon.scriptum.app.view.fragment.NotesFragment;
import sgtmelon.scriptum.office.annot.def.BinDef;

/**
 * ViewModel для {@link NotesFragment} и {@link BinFragment}
 */
public final class NotesViewModel extends AndroidViewModel {

    private List<NoteRepo> listNoteRepo;

    public NotesViewModel(@NonNull Application application) {
        super(application);
    }

    public List<NoteRepo> getListNoteRepo() {
        return listNoteRepo;
    }

    public void setListNoteRepo(List<NoteRepo> listNoteRepo) {
        this.listNoteRepo = listNoteRepo;
    }

    public List<NoteRepo> loadData(@BinDef int bin) {
        final Context context = getApplication().getApplicationContext();

        final RoomDb db = RoomDb.provideDb(context);
        listNoteRepo = db.daoNote().get(context, bin);
        db.close();

        return listNoteRepo;
    }

}
