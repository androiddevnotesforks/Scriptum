package sgtmelon.scriptum.screen.vm

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.room.converter.IntConverter
import sgtmelon.scriptum.screen.callback.PreferenceCallback

class PreferenceViewModel(context: Context, val callback: PreferenceCallback) {

    private val preference = PreferenceRepo(context)

    private val themeSummary: Array<String> =
            context.resources.getStringArray(R.array.text_app_theme)

    private val repeatSummary: Array<String> =
            context.resources.getStringArray(R.array.text_alarm_repeat)

    private val sortSummary: Array<String> =
            context.resources.getStringArray(R.array.text_note_sort)

    private val colorSummary: Array<String> =
            context.resources.getStringArray(R.array.text_note_color)

    private val saveTimeSummary: Array<String> =
            context.resources.getStringArray(R.array.text_save_time)

    fun updateSummary() = with(callback) {
        updateThemeSummary(themeSummary[preference.theme])
        updateRepeatSummary(repeatSummary[preference.repeat])
        updateSignalSummary(preference.getSignalSummary())
        updateSortSummary(sortSummary[preference.sort])
        updateColorSummary(colorSummary[preference.defaultColor])
        updateSaveTimeSummary(saveTimeSummary[preference.savePeriod])
    }

    fun onClickTheme(): Boolean {
        callback.showThemeDialog(preference.theme)
        return true
    }

    fun onResultTheme(@Theme theme: Int) {
        preference.theme = theme
        callback.updateThemeSummary(themeSummary[theme])
    }

    fun onClickRepeat(): Boolean {
        callback.showRepeatDialog(preference.repeat)
        return true
    }

    fun onResultRepeat(value : Int) {
        preference.repeat = value
        callback.updateRepeatSummary(repeatSummary[value])
    }

    fun onClickSignal(): Boolean {
        val array = IntConverter().toArray(preference.signal, PreferenceRepo.SIGNAL_ARRAY_SIZE)
        callback.showSignalDialog(array)
        return true
    }

    fun onResultSignal(array: BooleanArray) {
        preference.signal = IntConverter().toInt(array)
        callback.updateSignalSummary(preference.getSignalSummary())
    }

    fun onClickSort(): Boolean {
        callback.showSortDialog(preference.sort)
        return true
    }

    fun onResultNoteSort(value: Int) {
        preference.sort = value
        callback.updateSortSummary(sortSummary[value])
    }

    fun onClickNoteColor(): Boolean {
        callback.showColorDialog(preference.defaultColor)
        return true
    }

    fun onResultNoteColor(@Color value: Int) {
        preference.defaultColor = value
        callback.updateColorSummary(colorSummary[value])
    }

    fun onClickSaveTime(): Boolean {
        callback.showSaveTimeDialog(preference.savePeriod)
        return true
    }

    fun onResultSaveTime(value: Int) {
        preference.savePeriod = value
        callback.updateSaveTimeSummary(saveTimeSummary[value])
    }

}