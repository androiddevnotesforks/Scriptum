package sgtmelon.scriptum.screen.callback

import sgtmelon.scriptum.screen.view.pref.PrefFragment
import sgtmelon.scriptum.screen.vm.PrefViewModel

/**
 * Интерфейс для общения [PrefViewModel] с [PrefFragment]
 *
 * @author SerjantArbuz
 */
interface PrefCallback {

    fun updateSortSummary(summary: String)

    fun showSortDialog(sortKeys: String)

    fun updateColorSummary(summary: String)

    fun showColorDialog(check: Int)

    fun updateSaveTimeSummary(summary: String)

    fun showSaveTimeDialog(check: Int)

    fun updateThemePrefSummary(summary: String)

    fun showThemeDialog(check: Int)

}