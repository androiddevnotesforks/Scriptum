package sgtmelon.scriptum.app.vm.fragment;

import androidx.lifecycle.ViewModel;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.view.fragment.RollNoteFragment;
import sgtmelon.scriptum.app.view.fragment.TextNoteFragment;

/**
 * ViewModel для {@link TextNoteFragment} и {@link RollNoteFragment}
 */
public final class NoteFragmentViewModel extends ViewModel {

    private NoteRepo noteRepo;

    public NoteRepo getNoteRepo() {
        return noteRepo;
    }

    public void setNoteRepo(NoteRepo noteRepo) {
        this.noteRepo = noteRepo;
    }

    public boolean isEmpty() {
        return noteRepo == null;
    }

}
