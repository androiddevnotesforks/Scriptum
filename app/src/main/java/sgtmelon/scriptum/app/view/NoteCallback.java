package sgtmelon.scriptum.app.view;

import androidx.annotation.NonNull;
import sgtmelon.scriptum.app.control.SaveControl;
import sgtmelon.scriptum.app.vm.NoteViewModel;

/**
 * Интерфейс общения {@link NoteFragmentParent} и {@link NoteActivity}
 */
public interface NoteCallback {

    void setupFragment(boolean isSave);

    @NonNull
    SaveControl getSaveControl();

    @NonNull
    NoteViewModel getViewModel();

}