package sgtmelon.scriptum.presentation.screen.ui.callback.preference

import androidx.annotation.StringRes
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPrefFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IAlarmPrefViewModel

/**
 * Interface for communication [IAlarmPrefViewModel] with [AlarmPrefFragment].
 */
interface IAlarmPrefFragment {

    fun showToast(@StringRes stringId: Int)

    fun setup()

    fun updateRepeatSummary(summary: String?)

    fun showRepeatDialog(@Repeat value: Int)

    fun updateSignalSummary(summary: String?)

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