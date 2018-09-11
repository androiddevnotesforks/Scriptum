package sgtmelon.handynotes.app.viewModel;

import androidx.lifecycle.ViewModel;
import sgtmelon.handynotes.app.model.repo.RepoNote;

public class VmFrgTextRoll extends ViewModel {

    private RepoNote repoNote;

    public RepoNote getRepoNote() {
        return repoNote;
    }

    public void setRepoNote(RepoNote repoNote) {
        this.repoNote = repoNote;
    }

    public boolean isEmpty(){
        return repoNote == null;
    }

}
