package sgtmelon.scriptum.domain.interactor.callback.preference

import sgtmelon.scriptum.domain.interactor.impl.preference.AlarmPreferenceInteractor
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IAlarmPreferenceViewModel

/**
 * Interface for communication [IAlarmPreferenceViewModel] with [AlarmPreferenceInteractor].
 */
interface IAlarmPreferenceInteractor {

    @Repeat val repeat: Int

    fun getRepeatSummary(): String?

    fun updateRepeat(@Repeat value: Int): String?


    fun getSignalSummary(valueArray: BooleanArray): String?

    fun updateSignal(valueArray: BooleanArray): String?


    val volume: Int

    fun getVolumeSummary(): String

    fun updateVolume(value: Int): String

}