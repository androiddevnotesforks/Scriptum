package sgtmelon.scriptum.presentation.screen.ui.callback.preference

import androidx.annotation.StringRes
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.receiver.EternalReceiver
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPreferenceViewModel

/**
 * Interface for communication [IPreferenceViewModel] with [PreferenceFragment].
 */
interface IPreferenceFragment : EternalReceiver.Bridge.Bind {

    fun showToast(@StringRes stringId: Int)


    fun setupApp()

    fun setupNote()

    fun setupNotification()

    fun setupOther()

    fun setupDeveloper()


    fun updateThemeSummary(summary: String?)

    fun showThemeDialog(@Theme value: Int)

    //region Note functions

    fun updateSortSummary(summary: String?)

    fun showSortDialog(@Sort value: Int)

    fun updateColorSummary(summary: String?)

    fun showColorDialog(@Color color: Int)

    fun updateSavePeriodSummary(summary: String?)

    fun showSaveTimeDialog(value: Int)

    //endregion

    //region Notification functions

    fun updateRepeatSummary(summary: String?)

    fun showRepeatDialog(@Repeat value: Int)

    fun updateSignalSummary(summary: String?)

    fun showSignalDialog(valueArray: BooleanArray)

    fun showMelodyPermissionDialog()

    fun updateMelodyGroupEnabled(isEnabled: Boolean)

    fun updateMelodySummary(summary: String)

    fun showMelodyDialog(titleArray: Array<String>, value: Int)

    fun playMelody(stringUri: String)

    fun updateVolumeSummary(summary: String)

    fun showVolumeDialog(value: Int)

    //endregion

}