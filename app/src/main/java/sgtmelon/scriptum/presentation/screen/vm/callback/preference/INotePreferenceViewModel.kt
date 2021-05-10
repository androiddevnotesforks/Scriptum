package sgtmelon.scriptum.presentation.screen.vm.callback.preference

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.INotePreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.NotePreferenceViewModel

/**
 * Interface for communication [INotePreferenceFragment] with [NotePreferenceViewModel].
 */
interface INotePreferenceViewModel : IParentViewModel {

    fun onClickSort()

    fun onResultNoteSort(@Sort value: Int)

    fun onClickNoteColor()

    fun onResultNoteColor(@Color value: Int)

    fun onClickSaveTime()

    fun onResultSaveTime(value: Int)

}