package sgtmelon.handynotes.app.ui.vm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import sgtmelon.handynotes.app.db.DbRoom;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.app.model.repo.RepoRank;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.def.db.DefBin;

public class VmMain extends AndroidViewModel {

    public VmMain(@NonNull Application application) {
        super(application);
    }

    private RepoRank repoRank;
    private List<RepoNote> listRepoNote;
    private List<RepoNote> listRepoBin;

    public RepoRank getRepoRank() {
        return repoRank;
    }

    public List<RepoNote> getListRepoNote() {
        return listRepoNote;
    }

    public List<RepoNote> getListRepoBin() {
        return listRepoBin;
    }

    public void loadData() {
        Context context = getApplication().getApplicationContext();

        String order = Help.Pref.getSortNoteOrder(context);

        DbRoom db = DbRoom.provideDb(context);
        repoRank = db.daoRank().get();
        listRepoNote = db.daoNote().get(context, DefBin.out, order);
        listRepoBin = db.daoNote().get(context, DefBin.in, order);
        db.close();
    }

}
