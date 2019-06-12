package sgtmelon.scriptum.screen.callback

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.view.preference.PreferenceFragment
import sgtmelon.scriptum.screen.vm.PreferenceViewModel

/**
 * Интерфейс для общения [PreferenceViewModel] с [PreferenceFragment]
 *
 * @author SerjantArbuz
 */
interface PreferenceCallback {

    fun setupApp()

    fun setupNotification(melodyTitleList: Array<String>)

    fun setupNote()

    fun setupSave()

    fun setupOther()

    fun updateThemeSummary(summary: String)

    fun showThemeDialog(@Theme value: Int)

    fun updateRepeatSummary(summary: String)

    fun showRepeatDialog(value: Int)

    fun updateSignalSummary(summary: String)

    fun showSignalDialog(value: BooleanArray)

    fun updateMelodyGroupEnabled(enabled: Boolean)

    fun updateMelodySummary(summary: String)

    fun showMelodyDialog(value: Int)

    fun updateSortSummary(summary: String)

    fun showSortDialog(value: Int)

    fun updateColorSummary(summary: String)

    fun showColorDialog(@Color value: Int)

    fun updateSaveTimeSummary(summary: String)

    fun showSaveTimeDialog(value: Int)

}