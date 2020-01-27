package sgtmelon.scriptum.screen.vm

import android.content.Context
import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.toUri
import sgtmelon.scriptum.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.MelodyItem
import sgtmelon.scriptum.model.key.PermissionResult
import sgtmelon.scriptum.provider.SummaryProvider
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.room.converter.type.IntConverter
import sgtmelon.scriptum.screen.ui.callback.IPreferenceFragment
import sgtmelon.scriptum.screen.ui.preference.PreferenceFragment
import sgtmelon.scriptum.screen.vm.callback.IPreferenceViewModel

/**
 * ViewModel for [PreferenceFragment]
 */
class PreferenceViewModel(
        private val context: Context,
        private val signalInteractor: ISignalInteractor,
        private var callback: IPreferenceFragment?
) : IPreferenceViewModel {

    private val preferenceRepo: IPreferenceRepo = PreferenceRepo(context)

    private val summary = SummaryProvider(context)

    private val melodyList: List<MelodyItem> = signalInteractor.melodyList

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupApp()
            setupNote()
            setupNotification(melodyList.map { it.title }.toTypedArray())
            setupSave()
            setupOther()

            updateThemeSummary(summary.theme[preferenceRepo.theme])

            updateRepeatSummary(summary.repeat[preferenceRepo.repeat])
            updateSignalSummary(signalInteractor.getSignalSummary(summary.signal))
            updateMelodyGroupEnabled(signalInteractor.signalState.isMelody)
            updateMelodySummary(melodyList[signalInteractor.melodyCheck].title)
            updateVolumeSummary(context.resources.getString(R.string.pref_summary_alarm_volume, preferenceRepo.volume))

            updateSortSummary(summary.sort[preferenceRepo.sort])
            updateColorSummary(summary.color[preferenceRepo.defaultColor])
            updateSaveTimeSummary(summary.saveTime[preferenceRepo.savePeriod])
        }
    }

    override fun onDestroy(func: () -> Unit) {
        callback = null
    }


    override fun onClickTheme() = alwaysTrue {
        callback?.showThemeDialog(preferenceRepo.theme)
    }

    override fun onResultTheme(@Theme theme: Int) {
        preferenceRepo.theme = theme
        callback?.updateThemeSummary(summary.theme[theme])
    }


    override fun onClickSort() = alwaysTrue {
        callback?.showSortDialog(preferenceRepo.sort)
    }

    override fun onResultNoteSort(value: Int) {
        preferenceRepo.sort = value
        callback?.updateSortSummary(summary.sort[value])
    }

    override fun onClickNoteColor() = alwaysTrue {
        callback?.showColorDialog(preferenceRepo.defaultColor, preferenceRepo.theme)
    }

    override fun onResultNoteColor(@Color value: Int) {
        preferenceRepo.defaultColor = value
        callback?.updateColorSummary(summary.color[value])
    }


    override fun onClickRepeat() = alwaysTrue {
        callback?.showRepeatDialog(preferenceRepo.repeat)
    }

    override fun onResultRepeat(value: Int) {
        preferenceRepo.repeat = value
        callback?.updateRepeatSummary(summary.repeat[value])
    }

    override fun onClickSignal() = alwaysTrue {
        callback?.showSignalDialog(signalInteractor.signalCheck)
    }

    override fun onResultSignal(array: BooleanArray) {
        preferenceRepo.signal = IntConverter().toInt(array)
        callback?.apply {
            updateSignalSummary(signalInteractor.getSignalSummary(summary.signal))
            updateMelodyGroupEnabled(signalInteractor.signalState.isMelody)
        }
    }

    override fun onClickMelody(result: PermissionResult) = alwaysTrue {
        if (result == PermissionResult.ALLOWED) {
            callback?.showMelodyPermissionDialog()
        } else {
            callback?.showMelodyDialog(signalInteractor.melodyCheck)
        }
    }

    override fun onSelectMelody(item: Int) {
        callback?.playMelody(uri = melodyList[item].uri.toUri() ?: return)
    }

    override fun onResultMelody(value: Int) {
        signalInteractor.melodyUri = melodyList[value].uri
        callback?.updateMelodySummary(melodyList[value].title)
    }

    override fun onClickVolume() = alwaysTrue {
        callback?.showVolumeDialog(preferenceRepo.volume)
    }

    override fun onResultVolume(value: Int) {
        preferenceRepo.volume = value
        callback?.updateVolumeSummary(context.resources.getString(R.string.pref_summary_alarm_volume, value))
    }


    override fun onClickSaveTime() = alwaysTrue {
        callback?.showSaveTimeDialog(preferenceRepo.savePeriod)
    }

    override fun onResultSaveTime(value: Int) {
        preferenceRepo.savePeriod = value
        callback?.updateSaveTimeSummary(summary.saveTime[value])
    }


    private fun alwaysTrue(func: () -> Unit): Boolean {
        func()
        return true
    }

}