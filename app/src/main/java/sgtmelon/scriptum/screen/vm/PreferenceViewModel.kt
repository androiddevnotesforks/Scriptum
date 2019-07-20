package sgtmelon.scriptum.screen.vm

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.MelodyItem
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.room.converter.IntConverter
import sgtmelon.scriptum.screen.view.callback.IPreferenceFragment
import sgtmelon.scriptum.screen.callback.IPreferenceViewModel

class PreferenceViewModel(private val context: Context, var callback: IPreferenceFragment?) :
        IPreferenceViewModel {

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

    override fun onDestroy() {
        callback = null
    }

    override fun onSetup() {
        callback?.apply {
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
    }

    override fun onClickTheme() = alwaysTrue {
        callback?.showThemeDialog(iPreferenceRepo.theme)
    }

    override fun onResultTheme(@Theme theme: Int) {
        iPreferenceRepo.theme = theme
        callback?.updateThemeSummary(themeSummary[theme])
    }


    override fun onClickRepeat() = alwaysTrue {
        callback?.showRepeatDialog(iPreferenceRepo.repeat)
    }

    override fun onResultRepeat(value: Int) {
        iPreferenceRepo.repeat = value
        callback?.updateRepeatSummary(repeatSummary[value])
    }

    override fun onClickSignal() = alwaysTrue {
        val array = IntConverter().toArray(iPreferenceRepo.signal, PreferenceRepo.SIGNAL_ARRAY_SIZE)
        callback?.showSignalDialog(array)
    }

    override fun onResultSignal(array: BooleanArray) {
        iPreferenceRepo.signal = IntConverter().toInt(array)
        callback?.apply {
            updateSignalSummary(iPreferenceRepo.getSignalSummary())
            updateMelodyGroupEnabled(IntConverter().toArray(iPreferenceRepo.signal).first())
        }
    }

    override fun onClickMelody() = alwaysTrue {
        callback?.showMelodyDialog(getMelodyCheck())
    }

    override fun onResultMelody(value: Int) {
        iPreferenceRepo.melody = melodyUriList[value]
        callback?.updateMelodySummary(melodyList[value].title)
    }

    override fun onClickVolume() = alwaysTrue {
        callback?.showVolumeDialog(iPreferenceRepo.volume)
    }

    override fun onResultVolume(value: Int) {
        iPreferenceRepo.volume = value
        callback?.updateVolumeSummary(context.resources.getString(R.string.summary_alarm_volume, value))
    }


    override fun onClickSort() = alwaysTrue {
        callback?.showSortDialog(iPreferenceRepo.sort)
    }

    override fun onResultNoteSort(value: Int) {
        iPreferenceRepo.sort = value
        callback?.updateSortSummary(sortSummary[value])
    }

    override fun onClickNoteColor() = alwaysTrue {
        callback?.showColorDialog(iPreferenceRepo.defaultColor)
    }

    override fun onResultNoteColor(@Color value: Int) {
        iPreferenceRepo.defaultColor = value
        callback?.updateColorSummary(colorSummary[value])
    }

    override fun onClickSaveTime() = alwaysTrue {
        callback?.showSaveTimeDialog(iPreferenceRepo.savePeriod)
    }

    override fun onResultSaveTime(value: Int) {
        iPreferenceRepo.savePeriod = value
        callback?.updateSaveTimeSummary(saveTimeSummary[value])
    }

    companion object {
        private fun alwaysTrue(func: () -> Unit): Boolean {
            func()
            return true
        }
    }

}