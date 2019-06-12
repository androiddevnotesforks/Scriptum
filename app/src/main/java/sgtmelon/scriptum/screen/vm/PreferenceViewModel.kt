package sgtmelon.scriptum.screen.vm

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.room.converter.IntConverter
import sgtmelon.scriptum.screen.callback.PreferenceCallback

class PreferenceViewModel(context: Context, val callback: PreferenceCallback) {

    private val iPreferenceRepo = PreferenceRepo(context)

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

    fun onSetup() = with(callback) {
        iPreferenceRepo.getAlarmList()

        updateThemeSummary(themeSummary[iPreferenceRepo.theme])
        updateRepeatSummary(repeatSummary[iPreferenceRepo.repeat])
        updateSignalSummary(iPreferenceRepo.getSignalSummary())
        updateMelodyGroupEnabled(IntConverter().toArray(iPreferenceRepo.signal).first())

        updateSortSummary(sortSummary[iPreferenceRepo.sort])
        updateColorSummary(colorSummary[iPreferenceRepo.defaultColor])
        updateSaveTimeSummary(saveTimeSummary[iPreferenceRepo.savePeriod])
    }

    fun onClickTheme(): Boolean {
        callback.showThemeDialog(iPreferenceRepo.theme)
        return true
    }

    fun onResultTheme(@Theme theme: Int) {
        iPreferenceRepo.theme = theme
        callback.updateThemeSummary(themeSummary[theme])
    }

    fun onClickRepeat(): Boolean {
        callback.showRepeatDialog(iPreferenceRepo.repeat)
        return true
    }

    fun onResultRepeat(value : Int) {
        iPreferenceRepo.repeat = value
        callback.updateRepeatSummary(repeatSummary[value])
    }

    fun onClickSignal(): Boolean {
        val array = IntConverter().toArray(iPreferenceRepo.signal, PreferenceRepo.SIGNAL_ARRAY_SIZE)
        callback.showSignalDialog(array)
        return true
    }

    fun onResultSignal(array: BooleanArray) {
        iPreferenceRepo.signal = IntConverter().toInt(array)
        callback.updateSignalSummary(iPreferenceRepo.getSignalSummary())
        callback.updateMelodyGroupEnabled(IntConverter().toArray(iPreferenceRepo.signal).first())
    }

    fun onClickSort(): Boolean {
        callback.showSortDialog(iPreferenceRepo.sort)
        return true
    }

    fun onResultNoteSort(value: Int) {
        iPreferenceRepo.sort = value
        callback.updateSortSummary(sortSummary[value])
    }

    fun onClickNoteColor(): Boolean {
        callback.showColorDialog(iPreferenceRepo.defaultColor)
        return true
    }

    fun onResultNoteColor(@Color value: Int) {
        iPreferenceRepo.defaultColor = value
        callback.updateColorSummary(colorSummary[value])
    }

    fun onClickSaveTime(): Boolean {
        callback.showSaveTimeDialog(iPreferenceRepo.savePeriod)
        return true
    }

    fun onResultSaveTime(value: Int) {
        iPreferenceRepo.savePeriod = value
        callback.updateSaveTimeSummary(saveTimeSummary[value])
    }

}