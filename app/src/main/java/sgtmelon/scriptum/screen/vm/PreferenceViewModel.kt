package sgtmelon.scriptum.screen.vm

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.screen.callback.PreferenceCallback

class PreferenceViewModel(context: Context, val callback: PreferenceCallback) {

    private val preference = PreferenceRepo(context)

    private val themeSummary: Array<String> =
            context.resources.getStringArray(R.array.text_app_theme)

    private val alarmSummary: Array<String> =
            context.resources.getStringArray(R.array.text_alarm_repeat)

    private val sortSummary: Array<String> =
            context.resources.getStringArray(R.array.text_note_sort)

    private val colorSummary: Array<String> =
            context.resources.getStringArray(R.array.text_note_color)

    private val saveTimeSummary: Array<String> =
            context.resources.getStringArray(R.array.text_save_time)

    fun updateSummary() = with(callback) {
        updateAppThemeSummary(themeSummary[preference.getTheme()])
        updateAlarmRepeatSummary(alarmSummary[preference.getRepeat()])
        updateNoteSortSummary(sortSummary[preference.getSort()])
        updateNoteColorSummary(colorSummary[preference.getDefaultColor()])
        updateSaveTimeSummary(saveTimeSummary[preference.getSavePeriod()])
    }

    fun onClickAppTheme(): Boolean {
        callback.showAppThemeDialog(preference.getTheme())
        return true
    }

    fun onResultAppTheme(@Theme theme: Int) {
        preference.setTheme(theme)
        callback.updateAppThemeSummary(themeSummary[theme])
    }


    fun onClickAlarmRepeat(): Boolean {
        callback.showAlarmRepeatDialog(preference.getRepeat())
        return true
    }

    fun onResultAlarmRepeat(value : Int) {
        preference.setRepeat(value)
        callback.updateAlarmRepeatSummary(alarmSummary[value])
    }

    fun onClickNoteSort(): Boolean {
        callback.showNoteSortDialog(preference.getSort())
        return true
    }

    fun onResultNoteSort(value: Int) {
        preference.setSort(value)
        callback.updateNoteSortSummary(sortSummary[value])
    }

    fun onClickNoteColor(): Boolean {
        callback.showNoteColorDialog(preference.getDefaultColor())
        return true
    }

    fun onResultNoteColor(@Color color: Int) {
        preference.setDefaultColor(color)
        callback.updateNoteColorSummary(colorSummary[color])
    }

    fun onClickSaveTime(): Boolean {
        callback.showSaveTimeDialog(preference.getSavePeriod())
        return true
    }

    fun onResultSaveTime(check: Int) {
        preference.setSavePeriod(check)
        callback.updateSaveTimeSummary(saveTimeSummary[check])
    }

}