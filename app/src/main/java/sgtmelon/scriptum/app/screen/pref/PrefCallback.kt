package sgtmelon.scriptum.app.screen.pref

/**
 * Интерфейс для общения [PrefViewModel] с [PrefFragment]
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