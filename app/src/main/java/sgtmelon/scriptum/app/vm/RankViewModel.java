package sgtmelon.scriptum.app.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.RankModel;

public final class RankViewModel extends AndroidViewModel {

    private RankModel rankModel;

    public RankViewModel(@NonNull Application application) {
        super(application);
    }

    public RankModel getRankModel() {
        return rankModel;
    }

    public void setRankModel(RankModel rankModel) {
        this.rankModel = rankModel;
    }

    public RankModel loadData() {
        RoomDb db = RoomDb.provideDb(getApplication().getApplicationContext());
        rankModel = db.daoRank().get();
        db.close();

        return rankModel;
    }

}
