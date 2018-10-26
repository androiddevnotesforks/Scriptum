package sgtmelon.scriptum.app.vm.fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.RankModel;

public final class RankViewModel extends AndroidViewModel {

    private LiveData<RankModel> rankModel;

    public RankViewModel(@NonNull Application application) {
        super(application);
    }

    public RankModel getRankModel() {
        return rankModel.getValue();
    }

    public void setRankModel(RankModel rankModel) {
//        this.rankModel = rankModel;
    }

    public LiveData<RankModel> loadData() {
        if (rankModel == null) {
            RoomDb db = RoomDb.provideDb(getApplication().getApplicationContext());
            rankModel = db.daoRank().get();
            db.close();
        }

        return rankModel;
    }

}
