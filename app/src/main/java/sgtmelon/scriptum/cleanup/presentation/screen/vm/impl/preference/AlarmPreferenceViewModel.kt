package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IAlarmPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Repeat
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.domain.model.key.PermissionResult
import sgtmelon.common.utils.runBack
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IAlarmPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IAlarmPreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [IAlarmPreferenceFragment].
 */
class AlarmPreferenceViewModel(
    callback: IAlarmPreferenceFragment,
    private val interactor: IAlarmPreferenceInteractor,
    private val signalInteractor: ISignalInteractor
) : ParentViewModel<IAlarmPreferenceFragment>(callback),
    IAlarmPreferenceViewModel {

    override fun onSetup(bundle: Bundle?) {
        callback?.setup()

        callback?.updateRepeatSummary(interactor.getRepeatSummary())
        callback?.updateSignalSummary(interactor.getSignalSummary(signalInteractor.typeCheck))
        callback?.updateVolumeSummary(interactor.getVolumeSummary())

        viewModelScope.launch { setupBackground() }
    }

    @RunPrivate suspend fun setupBackground() {
        val state = signalInteractor.state ?: return

        /**
         * Make melody preference not enabled in every way, before we load data
         */
        callback?.updateMelodyGroupEnabled(state.isMelody)
        callback?.updateMelodyEnabled(isEnabled = false)

        callback?.startMelodySummarySearch()
        val melodyItem = runBack {
            val check = signalInteractor.getMelodyCheck() ?: return@runBack null
            val list = signalInteractor.getMelodyList()

            return@runBack list.getOrNull(check)
        }
        callback?.stopMelodySummarySearch()

        if (melodyItem != null) {
            callback?.updateMelodyEnabled(state.isMelody)
            callback?.updateMelodySummary(melodyItem.title)
        } else {
            /**
             * Melody preference not enabled in that case.
             */
            callback?.updateMelodySummary(R.string.pref_summary_alarm_melody_empty)
        }
    }

    /**
     * Need reset list, because user can change permission or
     * delete some files or remove sd card.
     *
     * It calls even after permission dialog.
     */
    override fun onPause() {
        signalInteractor.resetMelodyList()
    }

    override fun onClickRepeat() {
        callback?.showRepeatDialog(interactor.repeat)
    }

    override fun onResultRepeat(@Repeat value: Int) {
        callback?.updateRepeatSummary(interactor.updateRepeat(value))
    }

    override fun onClickSignal() {
        callback?.showSignalDialog(signalInteractor.typeCheck)
    }

    override fun onResultSignal(valueArray: BooleanArray) {
        callback?.updateSignalSummary(interactor.updateSignal(valueArray))

        val state = signalInteractor.state ?: return

        if (!state.isMelody) {
            callback?.updateMelodyGroupEnabled(isEnabled = false)
        } else {
            viewModelScope.launch {
                val melodyList = runBack { signalInteractor.getMelodyList() }

                callback?.updateMelodyGroupEnabled(isEnabled = true)

                if (melodyList.isEmpty()) {
                    callback?.updateMelodyEnabled(isEnabled = false)
                    callback?.updateMelodySummary(R.string.pref_summary_alarm_melody_empty)
                }
            }
        }
    }

    /**
     * Show permission only on [PermissionResult.ALLOWED] because we
     * can display melodies which not located on SD card.
     */
    override fun onClickMelody(result: PermissionResult?) {
        if (result == null) return

        when (result) {
            PermissionResult.ALLOWED -> callback?.showMelodyPermissionDialog()
            else -> viewModelScope.launch { prepareMelodyDialog() }
        }
    }

    @RunPrivate suspend fun prepareMelodyDialog() {
        val melodyList = runBack { signalInteractor.getMelodyList() }
        val melodyCheck = runBack { signalInteractor.getMelodyCheck() }

        val titleArray = melodyList.map { it.title }.toTypedArray()

        if (titleArray.isEmpty() || melodyCheck == null) {
            callback?.updateMelodyGroupEnabled(isEnabled = false)
        } else {
            callback?.showMelodyDialog(titleArray, melodyCheck)
        }
    }

    override fun onSelectMelody(value: Int) {
        viewModelScope.launch {
            val list = runBack { signalInteractor.getMelodyList() }
            val item = list.getOrNull(value) ?: return@launch

            callback?.playMelody(item.uri)
        }
    }

    override fun onResultMelody(title: String) {
        viewModelScope.launch {
            val resultTitle = runBack { signalInteractor.setMelodyUri(title) }

            when {
                title == resultTitle -> callback?.updateMelodySummary(title)
                resultTitle != null -> {
                    callback?.updateMelodySummary(resultTitle)
                    callback?.showToast(R.string.pref_toast_melody_replace)
                }
                else -> {
                    callback?.updateMelodyEnabled(isEnabled = false)
                    callback?.updateMelodySummary(R.string.pref_summary_alarm_melody_empty)
                }
            }
        }
    }

    override fun onClickVolume() {
        callback?.showVolumeDialog(interactor.volume)
    }

    override fun onResultVolume(value: Int) {
        callback?.updateVolumeSummary(interactor.updateVolume(value))
    }
}