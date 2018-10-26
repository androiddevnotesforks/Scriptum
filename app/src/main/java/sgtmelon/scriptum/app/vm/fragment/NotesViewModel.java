package sgtmelon.scriptum.app.vm.fragment;

import android.app.Application;
import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.office.annot.def.db.BinDef;

public final class NotesViewModel extends AndroidViewModel {

    private LiveData<List<Long>> rankVisible;
    private LiveData<List<NoteModel>> listModel;

    public NotesViewModel(@NonNull Application application) {
        super(application);
    }

    public List<NoteModel> getListModel() {
        return listModel.getValue();
    }

    public void setListModel(List<NoteModel> listModel) {
//        this.listModel = listModel;
    }

    public LiveData<List<Long>> getRankVisible(){
        if (rankVisible == null){
            Context context = getApplication().getApplicationContext();

            RoomDb db = RoomDb.provideDb(context);
            rankVisible = db.daoRank().getRankVisible();
            db.close();
        }
        return rankVisible;
    }

    public LiveData<List<NoteModel>> loadData(List<Long> rankVisible, @BinDef int bin) {
        if (listModel == null) {
            Context context = getApplication().getApplicationContext();

            RoomDb db = RoomDb.provideDb(context);
            listModel = db.daoNote().get(context, rankVisible, bin);
            db.close();
        }
        return listModel;
    }

}
