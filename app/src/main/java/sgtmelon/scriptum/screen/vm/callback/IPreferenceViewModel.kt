package sgtmelon.scriptum.screen.vm.callback

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.PermissionResult
import sgtmelon.scriptum.screen.ui.preference.PreferenceFragment
import sgtmelon.scriptum.screen.vm.PreferenceViewModel

/**
 * Interface for communication [PreferenceFragment] with [PreferenceViewModel]
 *
 * @author SerjantArbuz
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


    fun onClickExport(): Boolean

    fun onClickImport(result: PermissionResult): Boolean

    fun onResultImport(check: Int)

}