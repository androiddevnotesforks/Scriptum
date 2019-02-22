package sgtmelon.scriptum.app.screen.note;

import androidx.annotation.NonNull;
import sgtmelon.scriptum.app.control.SaveControl;

/**
 * Интерфейс общения с {@link NoteActivity}
 */
public interface NoteCallback {

    void setupFragment(boolean isSave);

    @NonNull
    SaveControl getSaveControl();

    @NonNull
    NoteViewModel getViewModel();

}