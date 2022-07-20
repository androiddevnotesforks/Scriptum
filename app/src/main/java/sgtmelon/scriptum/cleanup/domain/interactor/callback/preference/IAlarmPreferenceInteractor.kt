package sgtmelon.scriptum.cleanup.domain.interactor.callback.preference

import androidx.annotation.IntRange
import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.AlarmPreferenceInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IAlarmPreferenceViewModel

/**
 * Interface for communication [IAlarmPreferenceViewModel] with [AlarmPreferenceInteractor].
 */
interface IAlarmPreferenceInteractor {

    fun getSignalSummary(valueArray: BooleanArray): String?

    fun updateSignal(valueArray: BooleanArray): String?


    val volume: Int

    fun getVolumeSummary(): String

    fun updateVolume(@IntRange(from = 10, to = 100) value: Int): String

}