package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference

import androidx.annotation.IntRange
import sgtmelon.scriptum.cleanup.domain.model.key.PermissionResult
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IAlarmPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.AlarmPreferenceViewModel

/**
 * Interface for communication [IAlarmPreferenceFragment] with [AlarmPreferenceViewModel].
 */
interface IAlarmPreferenceViewModel : IParentViewModel {

    fun onPause()


    fun onClickRepeat()

    fun onResultRepeat(value: Int)

    fun onClickSignal()

    fun onResultSignal(valueArray: BooleanArray)

    fun onClickMelody(result: PermissionResult?)

    fun onSelectMelody(value: Int)

    fun onResultMelody(title: String)

    fun onClickVolume()

    fun onResultVolume(@IntRange(from = 10, to = 100) value: Int)

}