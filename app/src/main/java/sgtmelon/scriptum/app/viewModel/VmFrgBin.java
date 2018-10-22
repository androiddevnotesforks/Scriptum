package sgtmelon.scriptum.app.viewModel;

import android.app.Application;
import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.dataBase.DbRoom;
import sgtmelon.scriptum.app.model.repo.RepoNote;
import sgtmelon.scriptum.office.annot.def.db.DefBin;

public class VmFrgBin extends AndroidViewModel {

    public VmFrgBin(@NonNull Application application) {
        super(application);
    }

    private List<RepoNote> listRepo;

    public List<RepoNote> getListRepo() {
        return listRepo;
    }

    public void setListRepo(List<RepoNote> listRepo) {
        this.listRepo = listRepo;
    }

    public void clearListRepo() {
        listRepo.clear();
    }

    public List<RepoNote> loadData(@DefBin int bin) {
        Context context = getApplication().getApplicationContext();

        DbRoom db = DbRoom.provideDb(context);
        listRepo = db.daoNote().get(context, bin);
        db.close();

        return listRepo;
    }

}
