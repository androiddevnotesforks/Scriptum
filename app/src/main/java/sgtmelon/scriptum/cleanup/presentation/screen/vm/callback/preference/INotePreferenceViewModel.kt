package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference

import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.INotePreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.NotePreferenceViewModel

/**
 * Interface for communication [INotePreferenceFragment] with [NotePreferenceViewModel].
 */
interface INotePreferenceViewModel : IParentViewModel {

    fun onClickSort()

    fun onResultNoteSort(value: Int)

    fun onClickNoteColor()

    fun onResultNoteColor(value: Int)

    fun onClickSaveTime()

    fun onResultSaveTime(value: Int)

}