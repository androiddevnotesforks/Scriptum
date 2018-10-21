package sgtmelon.scriptum.app.view;

import sgtmelon.scriptum.app.control.SaveControl;
import sgtmelon.scriptum.app.vm.activity.ActivityNoteViewModel;

/**
 * Интерфейс общения {@link sgtmelon.scriptum.app.view.parent.NoteFragmentParent} и
 * {@link sgtmelon.scriptum.app.view.activity.NoteActivity}
 */
public interface NoteView {

    void setupFragment(boolean isSave);

    SaveControl getSaveControl();

    void setSaveControl(SaveControl saveControl);

    ActivityNoteViewModel getViewModel();

    void setViewModel(ActivityNoteViewModel viewModel);

}
