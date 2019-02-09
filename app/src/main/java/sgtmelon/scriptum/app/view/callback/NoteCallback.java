package sgtmelon.scriptum.app.view.callback;

import sgtmelon.scriptum.app.control.SaveControl;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.view.parent.NoteFragmentParent;
import sgtmelon.scriptum.app.vm.activity.NoteActivityViewModel;

/**
 * Интерфейс общения {@link NoteFragmentParent} и {@link NoteActivity}
 */
public interface NoteCallback {

    void setupFragment(boolean isSave);

    SaveControl getSaveControl();

    NoteActivityViewModel getViewModel();

}
