package sgtmelon.scriptum.screen.vm

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.MelodyItem
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.room.converter.IntConverter
import sgtmelon.scriptum.screen.callback.PreferenceCallback

class PreferenceViewModel(private val context: Context, val callback: PreferenceCallback) {

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

    private lateinit var melodyList: List<MelodyItem>

    private val melodyTitleList: List<String> by lazy {
        ArrayList<String>().apply { melodyList.forEach { add(it.title) } }
    }

    private val melodyUriList: List<String> by lazy {
        ArrayList<String>().apply { melodyList.forEach { add(it.uri) } }
    }

    private fun getMelodyCheck(): Int = with(iPreferenceRepo) {
        var check = melodyUriList.indexOf(melody)

        if (melody.isEmpty() || check == -1) {
            melody = melodyUriList.first()
            check = 0
        }

        return check
    }

    fun onSetup() = with(callback) {
        melodyList = iPreferenceRepo.getMelodyList()

        setupApp()
        setupNotification(melodyTitleList.toTypedArray())
        setupNote()
        setupSave()
        setupOther()

        updateThemeSummary(themeSummary[iPreferenceRepo.theme])

        updateRepeatSummary(repeatSummary[iPreferenceRepo.repeat])
        updateSignalSummary(iPreferenceRepo.getSignalSummary())
        updateMelodyGroupEnabled(IntConverter().toArray(iPreferenceRepo.signal).first())
        updateMelodySummary(melodyTitleList[getMelodyCheck()])
        updateVolumeSummary(context.resources.getString(R.string.summary_alarm_volume, iPreferenceRepo.volume))

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

    fun onResultRepeat(value: Int) {
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
        callback.apply {
            updateSignalSummary(iPreferenceRepo.getSignalSummary())
            updateMelodyGroupEnabled(IntConverter().toArray(iPreferenceRepo.signal).first())
        }
    }

    fun onClickMelody(): Boolean {
        callback.showMelodyDialog(getMelodyCheck())
        return true
    }

    fun onResultMelody(value: Int) {
        iPreferenceRepo.melody = melodyUriList[value]
        callback.updateMelodySummary(melodyList[value].title)
    }


    fun onClickVolume(): Boolean {
        callback.showVolumeDialog(iPreferenceRepo.volume)
        return true
    }

    fun onResultVolume(value: Int) {
        iPreferenceRepo.volume = value
        callback.updateVolumeSummary(context.resources.getString(R.string.summary_alarm_volume, value))
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