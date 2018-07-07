package sgtmelon.handynotes.app.viewModel;

import androidx.lifecycle.ViewModel;
import android.util.Log;

import sgtmelon.handynotes.app.model.repo.RepoNote;

public class VmFrgText extends ViewModel {

    private static final String TAG = "VmFrgText";

    private RepoNote repoNote;

    public RepoNote getRepoNote() {
        return repoNote;
    }

    public void setRepoNote(RepoNote repoNote) {
        this.repoNote = repoNote;
    }

    public boolean isEmpty(){
        Log.i(TAG, "isEmpty: " + (repoNote == null));
        return repoNote == null;
    }

}
