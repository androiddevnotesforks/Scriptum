package sgtmelon.handynotes.app.ui.vm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.app.db.DbRoom;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.item.ItemStatus;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.conv.ConvList;
import sgtmelon.handynotes.office.st.StNote;

public class VmNote extends AndroidViewModel {

    private DbRoom db;

    private StNote stNote;

    private List<Long> rankVisible;
    private RepoNote repoNote;

    public VmNote(@NonNull Application application, Bundle bundle) {
        super(application);

        if (bundle != null) {
            stNote.setCreate(bundle.getBoolean(StNote.KEY_CREATE));
            stNote.setEdit();

            db = DbRoom.provideDb(application.getApplicationContext());
            rankVisible = db.daoRank().getRankVisible();
            if (stNote.isCreate()) {
                ItemNote itemNote = new ItemNote(application.getApplicationContext(), bundle.getInt(Db.NT_TP));
                ItemStatus itemStatus = new ItemStatus(application.getApplicationContext(), itemNote, ConvList.fromList(rankVisible));

                repoNote = new RepoNote(itemNote, new ArrayList<ItemRoll>(), itemStatus);
            } else {
                repoNote = db.daoNote().get(application.getApplicationContext(), bundle.getLong(Db.NT_ID));
                stNote.setBin(repoNote.getItemNote().isBin());
            }
            db.close();
        }
    }

    public StNote getStNote() {
        return stNote;
    }

    public void setStNote(StNote stNote) {
        this.stNote = stNote;
    }

    public List<Long> getRankVisible() {
        return rankVisible;
    }

    public void setRankVisible(List<Long> rankVisible) {
        this.rankVisible = rankVisible;
    }

    public RepoNote getRepoNote() {
        return repoNote;
    }

    public void setRepoNote(RepoNote repoNote) {
        this.repoNote = repoNote;
    }

}
