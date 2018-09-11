package sgtmelon.scriptum.app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.dataBase.DbRoom;
import sgtmelon.scriptum.app.model.repo.RepoRank;

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
