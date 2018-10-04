package sgtmelon.scriptum.app.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.model.ModelRank;

public final class RankViewModel extends AndroidViewModel {

    private ModelRank modelRank;

    public RankViewModel(@NonNull Application application) {
        super(application);
    }

    public ModelRank getModelRank() {
        return modelRank;
    }

    public void setModelRank(ModelRank modelRank) {
        this.modelRank = modelRank;
    }

    public ModelRank loadData() {
        DbRoom db = DbRoom.provideDb(getApplication().getApplicationContext());
        modelRank = db.daoRank().get();
        db.close();

        return modelRank;
    }

}
