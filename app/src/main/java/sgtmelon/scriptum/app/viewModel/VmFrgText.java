package sgtmelon.scriptum.app.viewModel;

import androidx.lifecycle.ViewModel;
import sgtmelon.scriptum.app.model.repo.RepoNote;

public class VmFrgText extends ViewModel {

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
