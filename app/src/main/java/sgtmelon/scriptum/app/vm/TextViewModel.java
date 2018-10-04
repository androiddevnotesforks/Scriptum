package sgtmelon.scriptum.app.vm;

import androidx.lifecycle.ViewModel;
import sgtmelon.scriptum.app.model.ModelNote;

public final class TextViewModel extends ViewModel {

    private ModelNote modelNote;

    public ModelNote getModelNote() {
        return modelNote;
    }

    public void setModelNote(ModelNote modelNote) {
        this.modelNote = modelNote;
    }

    public boolean isEmpty() {
        return modelNote == null;
    }

}
