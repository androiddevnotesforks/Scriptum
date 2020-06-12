package sgtmelon.scriptum.presentation.screen.vm.impl

import android.os.Bundle
import sgtmelon.scriptum.domain.interactor.callback.IPreferenceInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.MelodyItem
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.presentation.screen.ui.callback.IPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IPreferenceViewModel

/**
 * ViewModel for [PreferenceFragment].
 */
class PreferenceViewModel(
        private val interactor: IPreferenceInteractor,
        private val signalInteractor: ISignalInteractor,
        @RunPrivate var callback: IPreferenceFragment?
) : IPreferenceViewModel {

    private val melodyList: List<MelodyItem> = signalInteractor.melodyList

    override fun onSetup(bundle: Bundle?) {
        val state = signalInteractor.state ?: return
        val melodyItem = melodyList.getOrNull(signalInteractor.melodyCheck) ?: return

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
            updateSignalSummary(interactor.getSignalSummary(signalInteractor.typeCheck))

            updateMelodyGroupEnabled(state.isMelody)
            updateMelodySummary(melodyItem.title)
            updateVolumeSummary(interactor.getVolumeSummary())
        }
    }

    override fun onDestroy(func: () -> Unit) {
        callback = null
    }


    override fun onClickTheme() = takeTrue { callback?.showThemeDialog(interactor.theme) }

    override fun onResultTheme(@Theme value: Int) {
        callback?.updateThemeSummary(interactor.updateTheme(value))
    }


    override fun onClickSort() = takeTrue { callback?.showSortDialog(interactor.sort) }

    override fun onResultNoteSort(@Sort value: Int) {
        callback?.updateSortSummary(interactor.updateSort(value))
    }

    override fun onClickNoteColor() = takeTrue {
        callback?.showColorDialog(interactor.defaultColor, interactor.theme)
    }

    override fun onResultNoteColor(@Color value: Int) {
        callback?.updateColorSummary(interactor.updateDefaultColor(value))
    }

    override fun onClickSaveTime() = takeTrue {
        callback?.showSaveTimeDialog(interactor.savePeriod)
    }

    override fun onResultSaveTime(value: Int) {
        callback?.updateSavePeriodSummary(interactor.updateSavePeriod(value))
    }


    override fun onClickRepeat() = takeTrue { callback?.showRepeatDialog(interactor.repeat) }

    override fun onResultRepeat(@Repeat value: Int) {
        callback?.updateRepeatSummary(interactor.updateRepeat(value))
    }

    override fun onClickSignal() = takeTrue { callback?.showSignalDialog(signalInteractor.typeCheck) }

    override fun onResultSignal(valueArray: BooleanArray) {
        val state = signalInteractor.state ?: return

        callback?.apply {
            updateSignalSummary(interactor.updateSignal(valueArray))
            updateMelodyGroupEnabled(state.isMelody)
        }
    }

    /**
     * Show permission only on [PermissionResult.ALLOWED] because we can display melodies which
     * not located on SD card.
     */
    override fun onClickMelody(result: PermissionResult) = takeTrue {
        when (result) {
            PermissionResult.ALLOWED -> callback?.showMelodyPermissionDialog()
            else -> callback?.showMelodyDialog(signalInteractor.melodyCheck)
        }
    }

    override fun onSelectMelody(value: Int) {
        val item = melodyList.getOrNull(value) ?: return

        callback?.playMelody(item.uri)
    }

    override fun onResultMelody(value: Int) {
        val item = melodyList.getOrNull(value) ?: return

        signalInteractor.setMelodyUri(item.uri)
        callback?.updateMelodySummary(item.title)
    }

    override fun onClickVolume() = takeTrue { callback?.showVolumeDialog(interactor.volume) }

    override fun onResultVolume(value: Int) {
        callback?.updateVolumeSummary(interactor.updateVolume(value))
    }


    private fun takeTrue(func: () -> Unit): Boolean {
        func()
        return true
    }

}