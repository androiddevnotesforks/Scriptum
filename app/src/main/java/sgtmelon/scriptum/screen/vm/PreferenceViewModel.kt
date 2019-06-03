package sgtmelon.scriptum.screen.vm

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.screen.callback.PreferenceCallback

class PreferenceViewModel(context: Context, val callback: PreferenceCallback) {

    private val preference = PreferenceRepo(context)

    private val themeSummary: Array<String> =
            context.resources.getStringArray(R.array.pref_theme_text)

    private val sortSummary: Array<String> =
            context.resources.getStringArray(R.array.pref_sort_text)

    private val colorSummary: Array<String> =
            context.resources.getStringArray(R.array.pref_color_text)

    private val saveTimeSummary: Array<String> =
            context.resources.getStringArray(R.array.pref_save_time_text)

    fun updateSummary() = with(callback) {
        updateThemePrefSummary(themeSummary[preference.getTheme()])
        updateSortSummary(sortSummary[preference.getSort()])
        updateColorSummary(colorSummary[preference.getDefaultColor()])
        updateSaveTimeSummary(saveTimeSummary[preference.getSavePeriod()])
    }

    fun onClickThemePreference(): Boolean {
        callback.showThemeDialog(preference.getTheme())
        return true
    }

    fun onResultThemeDialog(@Theme theme: Int) {
        preference.setTheme(theme)
        callback.updateThemePrefSummary(themeSummary[theme])
    }

    fun onClickSortPreference(): Boolean {
        callback.showSortDialog(preference.getSort())
        return true
    }

    fun onResultSortDialog(@Sort sort: Int) {
        preference.setSort(sort)
        callback.updateSortSummary(sortSummary[sort])
    }

    fun onClickColorPreference(): Boolean {
        callback.showColorDialog(preference.getDefaultColor())
        return true
    }

    fun onResultColorDialog(@Color color: Int) {
        preference.setDefaultColor(color)
        callback.updateColorSummary(colorSummary[color])
    }

    fun onClickSaveTimePreference(): Boolean {
        callback.showSaveTimeDialog(preference.getSavePeriod())
        return true
    }

    fun onResultSaveTimeDialog(check: Int) {
        preference.setSavePeriod(check)
        callback.updateSaveTimeSummary(saveTimeSummary[check])
    }

}