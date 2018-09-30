package sgtmelon.scriptum.app.viewModel;

import android.app.Application;
import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.model.repo.RepoNote;
import sgtmelon.scriptum.office.annot.def.db.DefBin;

public class VmFrgNotes extends AndroidViewModel {

    private List<RepoNote> listRepo;

    public VmFrgNotes(@NonNull Application application) {
        super(application);
    }

    public List<RepoNote> getListRepo() {
        return listRepo;
    }

    public void setListRepo(List<RepoNote> listRepo) {
        if (listRepo == null) this.listRepo.clear();
        else this.listRepo = listRepo;
    }

    public List<RepoNote> loadData(@DefBin int bin) {
        Context context = getApplication().getApplicationContext();

        DbRoom db = DbRoom.provideDb(context);
        listRepo = db.daoNote().get(context, bin);
        db.close();

        return listRepo;
    }

}
