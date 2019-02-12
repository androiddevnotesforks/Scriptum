package sgtmelon.scriptum.app.view.callback;

import androidx.annotation.NonNull;
import sgtmelon.scriptum.app.control.SaveControl;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.view.parent.NoteFragmentParent;
import sgtmelon.scriptum.app.vm.activity.NoteViewModel;

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