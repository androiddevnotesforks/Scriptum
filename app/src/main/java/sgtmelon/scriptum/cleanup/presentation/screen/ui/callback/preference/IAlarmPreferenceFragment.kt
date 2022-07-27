package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference

import androidx.annotation.StringRes
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IAlarmPreferenceViewModel
import sgtmelon.scriptum.infrastructure.model.key.Repeat

/**
 * Interface for communication [IAlarmPreferenceViewModel] with [AlarmPreferenceFragment].
 */
interface IAlarmPreferenceFragment {

    fun showToast(@StringRes stringId: Int)

    fun setup()

    fun updateRepeatSummary(summary: String)

    fun showRepeatDialog(repeat: Repeat)

    fun updateSignalSummary(summary: String)

    fun showSignalDialog(valueArray: BooleanArray)

    fun showMelodyPermissionDialog()

    fun updateMelodyEnabled(isEnabled: Boolean)

    fun updateMelodyGroupEnabled(isEnabled: Boolean)

    fun startMelodySummarySearch()

    fun stopMelodySummarySearch()

    fun updateMelodySummary(summary: String)

    fun updateMelodySummary(@StringRes summaryId: Int)

    fun showMelodyDialog(titleArray: Array<String>, value: Int)

    fun playMelody(stringUri: String)

    fun updateVolumeSummary(summary: String)

    fun showVolumeDialog(value: Int)

}