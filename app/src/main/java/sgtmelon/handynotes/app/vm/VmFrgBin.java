package sgtmelon.handynotes.app.vm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import sgtmelon.handynotes.app.db.DbRoom;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.def.db.DefBin;

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

    public void setListRepo() {
        listRepo.clear();
    }

    public List<RepoNote> loadData(@DefBin int bin) {
        Context context = getApplication().getApplicationContext();

        DbRoom db = DbRoom.provideDb(context);
        listRepo = db.daoNote().get(context, bin, Help.Pref.getSortNoteOrder(context));
        db.close();

        return listRepo;
    }

}
