package sgtmelon.scriptum.screen.callback

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.view.preference.PreferenceFragment
import sgtmelon.scriptum.screen.vm.PreferenceViewModel

/**
 * Интерфейс для общения [PreferenceFragment] с [PreferenceViewModel]
 *
 * @author SerjantArbuz
 */
interface IPreferenceViewModel {

    fun onSetup()

    fun onClickTheme(): Boolean

    fun onResultTheme(@Theme theme: Int)

    fun onClickRepeat(): Boolean

    fun onResultRepeat(value: Int)

    fun onClickSignal(): Boolean

    fun onResultSignal(array: BooleanArray)

    fun onClickMelody(): Boolean

    fun onResultMelody(value: Int)

    fun onClickVolume(): Boolean

    fun onResultVolume(value: Int)

    fun onClickSort(): Boolean

    fun onResultNoteSort(value: Int)

    fun onClickNoteColor(): Boolean

    fun onResultNoteColor(@Color value: Int)

    fun onClickSaveTime(): Boolean

    fun onResultSaveTime(value: Int)

}