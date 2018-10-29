package sgtmelon.scriptum.app.vm.fragment;

import androidx.lifecycle.ViewModel;
import sgtmelon.scriptum.app.model.NoteRepo;

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
