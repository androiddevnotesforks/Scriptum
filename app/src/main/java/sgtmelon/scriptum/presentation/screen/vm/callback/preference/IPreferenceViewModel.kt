package sgtmelon.scriptum.presentation.screen.vm.callback.preference

import sgtmelon.scriptum.domain.model.annotation.Repeat
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

    //region Notification functions

    fun onClickRepeat()

    fun onResultRepeat(@Repeat value: Int)

    fun onClickSignal()

    fun onResultSignal(valueArray: BooleanArray)

    fun onClickMelody(result: PermissionResult?)

    fun onSelectMelody(value: Int)

    fun onResultMelody(title: String)

    fun onClickVolume()

    fun onResultVolume(value: Int)

    //endregion

    fun onUnlockDeveloper()

}