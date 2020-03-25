package sgtmelon.scriptum.presentation.screen.vm.callback

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.PreferenceViewModel

/**
 * Interface for communication [PreferenceFragment] with [PreferenceViewModel].
 */
interface IPreferenceViewModel : IParentViewModel {

    fun onClickTheme(): Boolean

    fun onResultTheme(@Theme theme: Int)


    fun onClickSort(): Boolean

    fun onResultNoteSort(value: Int)

    fun onClickNoteColor(): Boolean

    fun onResultNoteColor(@Color value: Int)


    fun onClickRepeat(): Boolean

    fun onResultRepeat(value: Int)

    fun onClickSignal(): Boolean

    fun onResultSignal(array: BooleanArray)

    fun onClickMelody(result: PermissionResult): Boolean

    fun onSelectMelody(item: Int)

    fun onResultMelody(value: Int)

    fun onClickVolume(): Boolean

    fun onResultVolume(value: Int)


    fun onClickSaveTime(): Boolean

    fun onResultSaveTime(value: Int)

}