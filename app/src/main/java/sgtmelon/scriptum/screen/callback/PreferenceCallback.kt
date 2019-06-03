package sgtmelon.scriptum.screen.callback

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.view.preference.PreferenceFragment
import sgtmelon.scriptum.screen.vm.PreferenceViewModel

/**
 * Интерфейс для общения [PreferenceViewModel] с [PreferenceFragment]
 *
 * @author SerjantArbuz
 */
interface PreferenceCallback {

    fun updateThemePrefSummary(summary: String)

    fun showThemeDialog(@Theme value: Int)

    fun updateSortSummary(summary: String)

    fun showSortDialog(@Sort value: Int)

    fun updateColorSummary(summary: String)

    fun showColorDialog(@Color value: Int)

    fun updateSaveTimeSummary(summary: String)

    fun showSaveTimeDialog(value: Int)

}