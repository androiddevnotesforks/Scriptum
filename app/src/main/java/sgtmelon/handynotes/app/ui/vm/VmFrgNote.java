package sgtmelon.handynotes.app.ui.vm;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import sgtmelon.handynotes.app.model.repo.RepoNote;

public class VmFrgNote extends ViewModel {

    private static final String TAG = "VmFrgNote";

    private RepoNote repoNote;

    public RepoNote getRepoNote() {
        return repoNote;
    }

    public void setRepoNote(RepoNote repoNote) {
        this.repoNote = repoNote;
    }

    public boolean isEmpty(){
        Log.d(TAG, "isEmpty: " + (repoNote == null));
        return repoNote == null;
    }

}
