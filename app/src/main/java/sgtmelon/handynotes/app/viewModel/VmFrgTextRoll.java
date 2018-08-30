package sgtmelon.handynotes.app.viewModel;

import android.util.Log;

import androidx.lifecycle.ViewModel;
import sgtmelon.handynotes.app.model.repo.RepoNote;

public class VmFrgTextRoll extends ViewModel {

    private static final String TAG = "VmFrgTextRoll";

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
