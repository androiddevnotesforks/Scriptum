package sgtmelon.scriptum.presentation.screen.vm.callback

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.PreferenceViewModel

/**
 * Interface for communication [PreferenceFragment] with [PreferenceViewModel].
 */
interface IPreferenceViewModel : IParentViewModel {

    fun onClickTheme(): Boolean

    fun onResultTheme(@Theme value: Int)


    fun onClickSort(): Boolean

    fun onResultNoteSort(@Sort value: Int)

    fun onClickNoteColor(): Boolean

    fun onResultNoteColor(@Color value: Int)

    fun onClickSaveTime(): Boolean

    fun onResultSaveTime(value: Int)


    fun onClickRepeat(): Boolean

    fun onResultRepeat(@Repeat value: Int)

    fun onClickSignal(): Boolean

    fun onResultSignal(checkArray: BooleanArray)

    fun onClickMelody(result: PermissionResult): Boolean

    fun onSelectMelody(check: Int)

    fun onResultMelody(check: Int)

    fun onClickVolume(): Boolean

    fun onResultVolume(value: Int)

}