package sgtmelon.scriptum.app.vm;

import androidx.lifecycle.ViewModel;
import sgtmelon.scriptum.app.model.NoteModel;

public final class TextViewModel extends ViewModel {

    private NoteModel noteModel;

    public NoteModel getNoteModel() {
        return noteModel;
    }

    public void setNoteModel(NoteModel noteModel) {
        this.noteModel = noteModel;
    }

    public boolean isEmpty() {
        return noteModel == null;
    }

}
