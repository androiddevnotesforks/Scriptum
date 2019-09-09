package sgtmelon.scriptum.screen.vm

import android.content.Context
import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.extension.toUri
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.MelodyItem
import sgtmelon.scriptum.model.key.PermissionResult
import sgtmelon.scriptum.provider.SummaryProvider
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.room.converter.IntConverter
import sgtmelon.scriptum.screen.ui.callback.IPreferenceFragment
import sgtmelon.scriptum.screen.vm.callback.IPreferenceViewModel

class PreferenceViewModel(private val context: Context, var callback: IPreferenceFragment?) :
        IPreferenceViewModel {

    private val iPreferenceRepo: IPreferenceRepo = PreferenceRepo(context)

    private val summary = SummaryProvider(context)

    private val melodyList: List<MelodyItem> = iPreferenceRepo.melodyList

    override fun onDestroy(func: () -> Unit) {
        callback = null
    }

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupApp()
            setupNote()
            setupNotification(melodyList.map { it.title }.toTypedArray())
            setupSave()
            setupBackup()
            setupOther()

            updateThemeSummary(summary.theme[iPreferenceRepo.theme])

            updateRepeatSummary(summary.repeat[iPreferenceRepo.repeat])
            updateSignalSummary(iPreferenceRepo.signalSummary)
            updateMelodyGroupEnabled(iPreferenceRepo.signalState.isMelody)
            updateMelodySummary(melodyList[iPreferenceRepo.melodyCheck].title)
            updateVolumeSummary(context.resources.getString(R.string.summary_alarm_volume, iPreferenceRepo.volume))

            updateSortSummary(summary.sort[iPreferenceRepo.sort])
            updateColorSummary(summary.color[iPreferenceRepo.defaultColor])
            updateSaveTimeSummary(summary.saveTime[iPreferenceRepo.savePeriod])
        }
    }


    override fun onClickTheme() = alwaysTrue {
        callback?.showThemeDialog(iPreferenceRepo.theme)
    }

    override fun onResultTheme(@Theme theme: Int) {
        iPreferenceRepo.theme = theme
        callback?.updateThemeSummary(summary.theme[theme])
    }


    override fun onClickSort() = alwaysTrue {
        callback?.showSortDialog(iPreferenceRepo.sort)
    }

    override fun onResultNoteSort(value: Int) {
        iPreferenceRepo.sort = value
        callback?.updateSortSummary(summary.sort[value])
    }

    override fun onClickNoteColor() = alwaysTrue {
        callback?.showColorDialog(iPreferenceRepo.defaultColor)
    }

    override fun onResultNoteColor(@Color value: Int) {
        iPreferenceRepo.defaultColor = value
        callback?.updateColorSummary(summary.color[value])
    }


    override fun onClickRepeat() = alwaysTrue {
        callback?.showRepeatDialog(iPreferenceRepo.repeat)
    }

    override fun onResultRepeat(value: Int) {
        iPreferenceRepo.repeat = value
        callback?.updateRepeatSummary(summary.repeat[value])
    }

    override fun onClickSignal() = alwaysTrue {
        callback?.showSignalDialog(iPreferenceRepo.signalCheck)
    }

    override fun onResultSignal(array: BooleanArray) {
        iPreferenceRepo.signal = IntConverter().toInt(array)
        callback?.apply {
            updateSignalSummary(iPreferenceRepo.signalSummary)
            updateMelodyGroupEnabled(iPreferenceRepo.signalState.isMelody)
        }
    }

    override fun onClickMelody(result: PermissionResult) = alwaysTrue {
        if (result == PermissionResult.ALLOWED) {
            callback?.showMelodyPermissionDialog()
        } else {
            callback?.showMelodyDialog(iPreferenceRepo.melodyCheck)
        }
    }

    override fun onSelectMelody(item: Int) {
        callback?.playMelody(melodyList[item].uri.toUri())
    }

    override fun onResultMelody(value: Int) {
        iPreferenceRepo.melodyUri = melodyList[value].uri
        callback?.updateMelodySummary(melodyList[value].title)
    }

    override fun onClickVolume() = alwaysTrue {
        callback?.showVolumeDialog(iPreferenceRepo.volume)
    }

    override fun onResultVolume(value: Int) {
        iPreferenceRepo.volume = value
        callback?.updateVolumeSummary(context.resources.getString(R.string.summary_alarm_volume, value))
    }


    override fun onClickSaveTime() = alwaysTrue {
        callback?.showSaveTimeDialog(iPreferenceRepo.savePeriod)
    }

    override fun onResultSaveTime(value: Int) {
        iPreferenceRepo.savePeriod = value
        callback?.updateSaveTimeSummary(summary.saveTime[value])
    }


    override fun onClickExport() = alwaysTrue { context.showToast("Export") }

    override fun onClickImport() = alwaysTrue { context.showToast("Import") }

    private fun alwaysTrue(func: () -> Unit): Boolean {
        func()
        return true
    }

}