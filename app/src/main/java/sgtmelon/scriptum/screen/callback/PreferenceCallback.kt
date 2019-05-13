package sgtmelon.scriptum.screen.callback

import sgtmelon.scriptum.screen.view.preference.PreferenceFragment
import sgtmelon.scriptum.screen.vm.PreferenceViewModel

/**
 * Интерфейс для общения [PreferenceViewModel] с [PreferenceFragment]
 *
 * @author SerjantArbuz
 */
interface PreferenceCallback {

    fun updateSortSummary(summary: String)

    fun showSortDialog(sortKeys: String)

    fun updateColorSummary(summary: String)

    fun showColorDialog(check: Int)

    fun updateSaveTimeSummary(summary: String)

    fun showSaveTimeDialog(check: Int)

    fun updateThemePrefSummary(summary: String)

    fun showThemeDialog(check: Int)

}