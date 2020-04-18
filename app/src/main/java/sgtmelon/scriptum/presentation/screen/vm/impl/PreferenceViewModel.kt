package sgtmelon.scriptum.presentation.screen.vm.impl

import android.os.Bundle
import sgtmelon.scriptum.domain.interactor.callback.IPreferenceInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.MelodyItem
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.presentation.screen.ui.callback.IPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IPreferenceViewModel

/**
 * ViewModel for [PreferenceFragment]
 */
class PreferenceViewModel(
        private val preferenceInteractor: IPreferenceInteractor,
        private val signalInteractor: ISignalInteractor,
        private var callback: IPreferenceFragment?
) : IPreferenceViewModel {

    private val melodyList: List<MelodyItem> = signalInteractor.melodyList

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            setupApp()
            setupNote()
            setupNotification(melodyList.map { it.title }.toTypedArray())
            setupSave()
            setupOther()

            updateThemeSummary(preferenceInteractor.getThemeSummary())

            updateSortSummary(preferenceInteractor.getSortSummary())
            updateColorSummary(preferenceInteractor.getDefaultColorSummary())
            updateSavePeriodSummary(preferenceInteractor.getSavePeriodSummary())

            updateRepeatSummary(preferenceInteractor.getRepeatSummary())
            updateSignalSummary(preferenceInteractor.getSignalSummaryArray(signalInteractor.check))
            updateMelodyGroupEnabled(signalInteractor.state.isMelody)
            updateMelodySummary(melodyList[signalInteractor.melodyCheck].title)
            updateVolumeSummary(preferenceInteractor.getVolumeSummary())
        }
    }

    override fun onDestroy(func: () -> Unit) {
        callback = null
    }


    override fun onClickTheme() = alwaysTrue {
        callback?.showThemeDialog(preferenceInteractor.theme)
    }

    override fun onResultTheme(@Theme value: Int) {
        callback?.updateThemeSummary(preferenceInteractor.updateTheme(value))
    }


    override fun onClickSort() = alwaysTrue {
        callback?.showSortDialog(preferenceInteractor.sort)
    }

    override fun onResultNoteSort(@Sort value: Int) {
        callback?.updateSortSummary(preferenceInteractor.updateSort(value))
    }

    override fun onClickNoteColor() = alwaysTrue {
        callback?.showColorDialog(preferenceInteractor.defaultColor, preferenceInteractor.theme)
    }

    override fun onResultNoteColor(@Color value: Int) {
        callback?.updateColorSummary(preferenceInteractor.updateDefaultColor(value))
    }

    override fun onClickSaveTime() = alwaysTrue {
        callback?.showSaveTimeDialog(preferenceInteractor.savePeriod)
    }

    override fun onResultSaveTime(value: Int) {
        callback?.updateSavePeriodSummary(preferenceInteractor.updateSavePeriod(value))
    }


    override fun onClickRepeat() = alwaysTrue {
        callback?.showRepeatDialog(preferenceInteractor.repeat)
    }

    override fun onResultRepeat(@Repeat value: Int) {
        callback?.updateRepeatSummary(preferenceInteractor.updateRepeat(value))
    }

    override fun onClickSignal() = alwaysTrue {
        callback?.showSignalDialog(signalInteractor.check)
    }

    override fun onResultSignal(checkArray: BooleanArray) {
        callback?.apply {
            updateSignalSummary(preferenceInteractor.updateSignal(checkArray))
            updateMelodyGroupEnabled(signalInteractor.state.isMelody)
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
        callback?.playMelody(melodyList[item].uri)
    }

    override fun onResultMelody(value: Int) {
        signalInteractor.melodyUri = melodyList[value].uri
        callback?.updateMelodySummary(melodyList[value].title)
    }

    override fun onClickVolume() = alwaysTrue {
        callback?.showVolumeDialog(preferenceInteractor.volume)
    }

    override fun onResultVolume(value: Int) {
        callback?.updateVolumeSummary(preferenceInteractor.updateVolume(value))
    }


    private fun alwaysTrue(func: () -> Unit): Boolean {
        func()
        return true
    }

}