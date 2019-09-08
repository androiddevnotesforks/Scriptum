package sgtmelon.scriptum.screen.ui.callback

import android.net.Uri
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.preference.PreferenceFragment
import sgtmelon.scriptum.screen.vm.PreferenceViewModel

/**
 * Interface for communication [PreferenceViewModel] with [PreferenceFragment]
 *
 * @author SerjantArbuz
 */
interface IPreferenceFragment {

    fun setupApp()

    fun setupNote()

    fun setupNotification(melodyTitleList: Array<String>)

    fun setupSave()

    fun setupOther()

    fun updateThemeSummary(summary: String)

    fun showThemeDialog(@Theme value: Int)


    fun updateSortSummary(summary: String)

    fun showSortDialog(value: Int)

    fun updateColorSummary(summary: String)

    fun showColorDialog(@Color value: Int)


    fun updateRepeatSummary(summary: String)

    fun showRepeatDialog(value: Int)

    fun updateSignalSummary(summary: String)

    fun showSignalDialog(value: BooleanArray)

    fun showMelodyPermissionDialog()

    fun updateMelodyGroupEnabled(enabled: Boolean)

    fun updateMelodySummary(summary: String)

    fun showMelodyDialog(value: Int)

    fun playMelody(uri: Uri)

    fun updateVolumeSummary(summary: String)

    fun showVolumeDialog(value: Int)


    fun updateSaveTimeSummary(summary: String)

    fun showSaveTimeDialog(value: Int)

}