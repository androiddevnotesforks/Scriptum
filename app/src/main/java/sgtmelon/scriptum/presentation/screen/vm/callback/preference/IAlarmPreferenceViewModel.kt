package sgtmelon.scriptum.presentation.screen.vm.callback.preference

import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IAlarmPreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.AlarmPreferenceViewModel

/**
 * Interface for communication [IAlarmPreferenceFragment] with [AlarmPreferenceViewModel].
 */
interface IAlarmPreferenceViewModel : IParentViewModel {

    fun onPause()


    fun onClickRepeat()

    fun onResultRepeat(@Repeat value: Int)

    fun onClickSignal()

    fun onResultSignal(valueArray: BooleanArray)

    fun onClickMelody(result: PermissionResult?)

    fun onSelectMelody(value: Int)

    fun onResultMelody(title: String)

    fun onClickVolume()

    fun onResultVolume(value: Int)

}