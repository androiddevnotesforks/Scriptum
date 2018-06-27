package sgtmelon.handynotes.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import sgtmelon.handynotes.app.data.DataRoom;
import sgtmelon.handynotes.app.model.item.ItemNote;

// TODO: 27.06.2018 Подумай над концепцией MVVM

public class ViewModelFrgBin extends AndroidViewModel {

    public ViewModelFrgBin(@NonNull Application application) {
        super(application);
    }

    private DataRoom db;
    private LiveData<List<ItemNote>> listNote;

    public LiveData<List<ItemNote>> getData(){
        if(listNote == null){
//            listNote =
        }
        return listNote;
    }

}
