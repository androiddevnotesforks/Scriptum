package sgtmelon.handynotes.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRank;
import sgtmelon.handynotes.app.model.manager.ManagerRoll;
import sgtmelon.handynotes.app.model.manager.ManagerStatus;

// TODO: 27.06.2018 Общая модель для каждой активити, в которой будут храниться необходимые данные для всех фрагментов
public class ViewModelMain extends AndroidViewModel {

    public ViewModelMain(@NonNull Application application) {
        super(application);
    }

    public ManagerRoll managerRoll;
    public ManagerStatus managerStatus;

    private List<ItemRank> listRank;
    private List<ItemNote> listNote;
    private List<ItemNote> listBin;



}
