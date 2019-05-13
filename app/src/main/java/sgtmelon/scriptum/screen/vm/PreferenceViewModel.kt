package sgtmelon.scriptum.screen.vm

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.annot.def.SortDef
import sgtmelon.scriptum.office.utils.Preference
import sgtmelon.scriptum.screen.callback.PreferenceCallback

class PreferenceViewModel(context: Context, val callback: PreferenceCallback) {

    private val preference = Preference(context)

    private val colorSummary: Array<String> =
            context.resources.getStringArray(R.array.pref_color_text)

    private val saveTimeSummary: Array<String> =
            context.resources.getStringArray(R.array.pref_save_time_text)

    private val themeSummary: Array<String> =
            context.resources.getStringArray(R.array.pref_theme_text)

    fun updateSummary() {
        callback.updateSortSummary(preference.getSortSummary())
        callback.updateColorSummary(colorSummary[preference.defaultColor])
        callback.updateSaveTimeSummary(saveTimeSummary[preference.saveTime])
        callback.updateThemePrefSummary(themeSummary[preference.theme])
    }

    fun onClickSortPreference(): Boolean {
        callback.showSortDialog(preference.sort)
        return true
    }

    fun onResultSortDialog(sortKeys: String = SortDef.def) {
        preference.sort = sortKeys
        callback.updateSortSummary(preference.getSortSummary())
    }

    fun onClickColorPreference(): Boolean {
        callback.showColorDialog(preference.defaultColor)
        return true
    }

    fun onResultColorDialog(check: Int) {
        preference.defaultColor = check
        callback.updateColorSummary(colorSummary[check])
    }

    fun onClickSaveTimePreference(): Boolean {
        callback.showSaveTimeDialog(preference.saveTime)
        return true
    }

    fun onResultSaveTimeDialog(check: Int) {
        preference.saveTime = check
        callback.updateSaveTimeSummary(saveTimeSummary[check])
    }

    fun onClickThemePreference(): Boolean {
        callback.showThemeDialog(preference.theme)
        return true
    }

    fun onResultThemeDialog(check: Int) {
        preference.theme = check
        callback.updateThemePrefSummary(themeSummary[check])
    }

}