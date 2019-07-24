package sgtmelon.scriptum.screen.vm.callback

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.preference.PreferenceFragment
import sgtmelon.scriptum.screen.vm.PreferenceViewModel

/**
 * Interface for communication [PreferenceFragment] with [PreferenceViewModel]
 *
 * @author SerjantArbuz
 */
interface IPreferenceViewModel : IParentViewModel {

    fun onSetup()

    fun onClickTheme(): Boolean

    fun onResultTheme(@Theme theme: Int)

    fun onClickRepeat(): Boolean

    fun onResultRepeat(value: Int)

    fun onClickSignal(): Boolean

    fun onResultSignal(array: BooleanArray)

    fun onClickMelody(): Boolean

    fun onSelectMelody(item: Int)

    fun onResultMelody(value: Int)

    fun onDismissMelody()

    fun onClickVolume(): Boolean

    fun onResultVolume(value: Int)

    fun onClickSort(): Boolean

    fun onResultNoteSort(value: Int)

    fun onClickNoteColor(): Boolean

    fun onResultNoteColor(@Color value: Int)

    fun onClickSaveTime(): Boolean

    fun onResultSaveTime(value: Int)

}