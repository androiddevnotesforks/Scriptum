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
        private val interactor: IPreferenceInteractor,
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

            updateThemeSummary(interactor.getThemeSummary())

            updateSortSummary(interactor.getSortSummary())
            updateColorSummary(interactor.getDefaultColorSummary())
            updateSavePeriodSummary(interactor.getSavePeriodSummary())

            updateRepeatSummary(interactor.getRepeatSummary())

            val check = signalInteractor.check ?: return
            updateSignalSummary(interactor.getSignalSummaryArray(check))

            val state = signalInteractor.state ?: return
            updateMelodyGroupEnabled(state.isMelody)
            updateMelodySummary(melodyList[signalInteractor.melodyCheck].title)
            updateVolumeSummary(interactor.getVolumeSummary())
        }
    }

    override fun onDestroy(func: () -> Unit) {
        callback = null
    }


    override fun onClickTheme() = alwaysTrue {
        val theme = interactor.theme ?: return@alwaysTrue
        callback?.showThemeDialog(theme)
    }

    override fun onResultTheme(@Theme value: Int) {
        callback?.updateThemeSummary(interactor.updateTheme(value))
    }


    override fun onClickSort() = alwaysTrue {
        val sort = interactor.sort ?: return@alwaysTrue
        callback?.showSortDialog(sort)
    }

    override fun onResultNoteSort(@Sort value: Int) {
        callback?.updateSortSummary(interactor.updateSort(value))
    }

    override fun onClickNoteColor() = alwaysTrue {
        val theme = interactor.theme ?: return@alwaysTrue
        val defaultColor = interactor.defaultColor ?: return@alwaysTrue

        callback?.showColorDialog(defaultColor, theme)
    }

    override fun onResultNoteColor(@Color value: Int) {
        callback?.updateColorSummary(interactor.updateDefaultColor(value))
    }

    override fun onClickSaveTime() = alwaysTrue {
        val savePeriod = interactor.savePeriod ?: return@alwaysTrue
        callback?.showSaveTimeDialog(savePeriod)
    }

    override fun onResultSaveTime(value: Int) {
        callback?.updateSavePeriodSummary(interactor.updateSavePeriod(value))
    }


    override fun onClickRepeat() = alwaysTrue {
        val repeat = interactor.repeat ?: return@alwaysTrue
        callback?.showRepeatDialog(repeat)
    }

    override fun onResultRepeat(@Repeat value: Int) {
        callback?.updateRepeatSummary(interactor.updateRepeat(value))
    }

    override fun onClickSignal() = alwaysTrue {
        val check = signalInteractor.check ?: return@alwaysTrue
        callback?.showSignalDialog(check)
    }

    override fun onResultSignal(checkArray: BooleanArray) {
        val state = signalInteractor.state ?: return

        callback?.apply {
            updateSignalSummary(interactor.updateSignal(checkArray))
            updateMelodyGroupEnabled(state.isMelody)
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
        signalInteractor.setMelodyUri(melodyList[value].uri)
        callback?.updateMelodySummary(melodyList[value].title)
    }

    override fun onClickVolume() = alwaysTrue {
        val volume = interactor.volume ?: return@alwaysTrue
        callback?.showVolumeDialog(volume)
    }

    override fun onResultVolume(value: Int) {
        callback?.updateVolumeSummary(interactor.updateVolume(value))
    }


    private fun alwaysTrue(func: () -> Unit): Boolean {
        func()
        return true
    }

}