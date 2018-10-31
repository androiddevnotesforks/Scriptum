package sgtmelon.scriptum.app.vm.fragment;

import androidx.lifecycle.ViewModel;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.view.fragment.RollFragment;
import sgtmelon.scriptum.app.view.fragment.TextFragment;

/**
 * ViewModel для {@link TextFragment} и {@link RollFragment}
 */
public final class FragmentNoteViewModel extends ViewModel {

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
