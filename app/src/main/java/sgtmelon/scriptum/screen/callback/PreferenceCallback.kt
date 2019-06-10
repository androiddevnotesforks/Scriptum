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

    fun updateAppThemeSummary(summary: String)

    fun showAppThemeDialog(@Theme value: Int)

    fun updateAlarmRepeatSummary(summary: String)

    fun showAlarmRepeatDialog(value: Int)

    fun updateNoteSortSummary(summary: String)

    fun showNoteSortDialog(value: Int)

    fun updateNoteColorSummary(summary: String)

    fun showNoteColorDialog(@Color value: Int)

    fun updateSaveTimeSummary(summary: String)

    fun showSaveTimeDialog(value: Int)

}