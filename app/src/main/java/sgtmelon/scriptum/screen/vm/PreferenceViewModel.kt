package sgtmelon.scriptum.screen.vm

import android.content.Context
import android.media.MediaPlayer
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.toUri
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.MelodyItem
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.room.converter.IntConverter
import sgtmelon.scriptum.screen.ui.callback.IPreferenceFragment
import sgtmelon.scriptum.screen.vm.callback.IPreferenceViewModel

class PreferenceViewModel(private val context: Context, var callback: IPreferenceFragment?) :
        IPreferenceViewModel {

    private val iPreferenceRepo = PreferenceRepo(context)

    private val themeSummary = context.resources.getStringArray(R.array.text_app_theme)
    private val repeatSummary = context.resources.getStringArray(R.array.text_alarm_repeat)
    private val sortSummary = context.resources.getStringArray(R.array.text_note_sort)
    private val colorSummary = context.resources.getStringArray(R.array.text_note_color)
    private val saveTimeSummary = context.resources.getStringArray(R.array.text_save_time)

    private val melodyList: List<MelodyItem> = iPreferenceRepo.melodyList

    private var melodyPlayer: MediaPlayer? = null

    override fun onDestroy() {
        callback = null
    }

    override fun onSetup() {
        callback?.apply {
            setupApp()
            setupNotification(melodyList.map { it.title }.toTypedArray())
            setupNote()
            setupSave()
            setupOther()

            updateThemeSummary(themeSummary[iPreferenceRepo.theme])

            updateRepeatSummary(repeatSummary[iPreferenceRepo.repeat])
            updateSignalSummary(iPreferenceRepo.signalSummary)
            updateMelodyGroupEnabled(IntConverter().toArray(iPreferenceRepo.signal).first())
            updateMelodySummary(melodyList[iPreferenceRepo.melodyCheck].title)
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
            updateSignalSummary(iPreferenceRepo.signalSummary)
            updateMelodyGroupEnabled(IntConverter().toArray(iPreferenceRepo.signal).first())
        }
    }

    override fun onClickMelody() =
            alwaysTrue { callback?.showMelodyDialog(iPreferenceRepo.melodyCheck) }

    override fun onSelectMelody(item: Int) {
        melodyPlayer?.stop()

        melodyPlayer = MediaPlayer.create(context, melodyList[item].uri.toUri())
        melodyPlayer?.start()
    }

    override fun onResultMelody(value: Int) {
        iPreferenceRepo.melodyUri = melodyList[value].uri
        callback?.updateMelodySummary(melodyList[value].title)
    }

    override fun onDismissMelody() {
        melodyPlayer?.stop()
    }

    override fun onClickVolume() = alwaysTrue { callback?.showVolumeDialog(iPreferenceRepo.volume) }

    override fun onResultVolume(value: Int) {
        iPreferenceRepo.volume = value
        callback?.updateVolumeSummary(context.resources.getString(R.string.summary_alarm_volume, value))
    }


    override fun onClickSort() = alwaysTrue { callback?.showSortDialog(iPreferenceRepo.sort) }

    override fun onResultNoteSort(value: Int) {
        iPreferenceRepo.sort = value
        callback?.updateSortSummary(sortSummary[value])
    }

    override fun onClickNoteColor() =
            alwaysTrue { callback?.showColorDialog(iPreferenceRepo.defaultColor) }

    override fun onResultNoteColor(@Color value: Int) {
        iPreferenceRepo.defaultColor = value
        callback?.updateColorSummary(colorSummary[value])
    }

    override fun onClickSaveTime() =
            alwaysTrue { callback?.showSaveTimeDialog(iPreferenceRepo.savePeriod) }

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