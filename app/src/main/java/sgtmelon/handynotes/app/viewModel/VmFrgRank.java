package sgtmelon.handynotes.app.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

import sgtmelon.handynotes.app.dataBase.DbRoom;
import sgtmelon.handynotes.app.model.repo.RepoRank;

public class VmFrgRank extends AndroidViewModel {

    public VmFrgRank(@NonNull Application application) {
        super(application);
    }

    private RepoRank repoRank;

    public RepoRank getRepoRank() {
        return repoRank;
    }

    public void setRepoRank(RepoRank repoRank) {
        this.repoRank = repoRank;
    }

    public RepoRank loadData() {
        DbRoom db = DbRoom.provideDb(getApplication().getApplicationContext());
        repoRank = db.daoRank().get();
        db.close();

        return repoRank;
    }

}
