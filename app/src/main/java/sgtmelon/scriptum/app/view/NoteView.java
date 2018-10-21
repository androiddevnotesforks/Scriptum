package sgtmelon.scriptum.app.view;

import sgtmelon.scriptum.app.control.SaveControl;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.view.parent.NoteFragmentParent;
import sgtmelon.scriptum.app.vm.activity.ActivityNoteViewModel;

/**
 * Интерфейс общения {@link NoteFragmentParent} и
 * {@link NoteActivity}
 */
public interface NoteView {

    void setupFragment(boolean isSave);

    SaveControl getSaveControl();

    void setSaveControl(SaveControl saveControl);

    ActivityNoteViewModel getViewModel();

    void setViewModel(ActivityNoteViewModel viewModel);

}
