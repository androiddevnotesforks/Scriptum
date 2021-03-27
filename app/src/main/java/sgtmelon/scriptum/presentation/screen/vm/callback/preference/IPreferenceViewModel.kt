package sgtmelon.scriptum.presentation.screen.vm.callback.preference

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IPreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.PreferenceViewModel

/**
 * Interface for communication [IPreferenceFragment] with [PreferenceViewModel].
 */
interface IPreferenceViewModel : IParentViewModel {

    fun onPause()


    fun onClickTheme()

    fun onResultTheme(@Theme value: Int)


    fun onClickExport(result: PermissionResult?)

    fun onClickImport(result: PermissionResult?)

    fun onResultImport(name: String)


    fun onClickSort()

    fun onResultNoteSort(@Sort value: Int)

    fun onClickNoteColor()

    fun onResultNoteColor(@Color value: Int)

    fun onClickSaveTime()

    fun onResultSaveTime(value: Int)


    fun onClickRepeat()

    fun onResultRepeat(@Repeat value: Int)

    fun onClickSignal()

    fun onResultSignal(valueArray: BooleanArray)

    fun onClickMelody(result: PermissionResult?)

    fun onSelectMelody(value: Int)

    fun onResultMelody(title: String)

    fun onClickVolume()

    fun onResultVolume(value: Int)


    fun onUnlockDeveloper()

}