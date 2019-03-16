package sgtmelon.scriptum.app.screen.pref

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.annot.def.SortDef
import sgtmelon.scriptum.office.utils.Preference

class PrefViewModel(context: Context, val callback: PrefCallback) {

    private val prefUtils = Preference(context)

    private val colorSummary: Array<String> =
            context.resources.getStringArray(R.array.pref_color_text)

    private val saveTimeSummary: Array<String> =
            context.resources.getStringArray(R.array.pref_save_time_text)

    private val themeSummary: Array<String> =
            context.resources.getStringArray(R.array.pref_theme_text)

    fun updateSummary() {
        callback.updateSortSummary(prefUtils.getSortSummary())
        callback.updateColorSummary(colorSummary[prefUtils.defaultColor])
        callback.updateSaveTimeSummary(saveTimeSummary[prefUtils.saveTime])
        callback.updateThemePrefSummary(themeSummary[prefUtils.theme])
    }

    fun onClickSortPreference(): Boolean {
        callback.showSortDialog(prefUtils.sort)
        return true
    }

    fun onResultSortDialog(sortKeys: String = SortDef.def) {
        prefUtils.sort = sortKeys
        callback.updateSortSummary(prefUtils.getSortSummary())
    }

    fun onClickColorPreference(): Boolean {
        callback.showColorDialog(prefUtils.defaultColor)
        return true
    }

    fun onResultColorDialog(check: Int) {
        prefUtils.defaultColor = check
        callback.updateColorSummary(colorSummary[check])
    }

    fun onClickSaveTimePreference(): Boolean {
        callback.showSaveTimeDialog(prefUtils.saveTime)
        return true
    }

    fun onResultSaveTimeDialog(check: Int) {
        prefUtils.saveTime = check
        callback.updateSaveTimeSummary(saveTimeSummary[check])
    }

    fun onClickThemePreference(): Boolean {
        callback.showThemeDialog(prefUtils.theme)
        return true
    }

    fun onResultThemeDialog(check: Int) {
        prefUtils.theme = check
        callback.updateThemePrefSummary(themeSummary[check])
    }

}