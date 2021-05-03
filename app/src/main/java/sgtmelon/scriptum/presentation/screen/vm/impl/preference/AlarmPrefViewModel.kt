package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.interactor.callback.preference.IAlarmPrefInteractor
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.extension.runBack
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IAlarmPrefFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IAlarmPrefViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [IAlarmPrefFragment].
 */
class AlarmPrefViewModel(
    application: Application
) : ParentViewModel<IAlarmPrefFragment>(application),
    IAlarmPrefViewModel {

    private lateinit var interactor: IAlarmPrefInteractor
    private lateinit var signalInteractor: ISignalInteractor

    fun setInteractor(interactor: IAlarmPrefInteractor, signalInteractor: ISignalInteractor) {
        this.interactor = interactor
        this.signalInteractor = signalInteractor
    }

    override fun onSetup(bundle: Bundle?) {
        callback?.setup()

        callback?.updateRepeatSummary(interactor.getRepeatSummary())
        callback?.updateSignalSummary(interactor.getSignalSummary(signalInteractor.typeCheck))

        /**
         * Make melody permissions not enabled before [setupBackground] load data.
         */
        callback?.updateMelodyGroupEnabled(isEnabled = false)
        callback?.updateVolumeSummary(interactor.getVolumeSummary())

        viewModelScope.launch { setupBackground() }
    }

    @RunPrivate suspend fun setupBackground() {
        val state = signalInteractor.state ?: return

        fun onEmptyError() {
            if (!state.isMelody) return

            callback?.showToast(R.string.pref_toast_melody_empty)
        }

        val melodyItem = runBack {
            val check = signalInteractor.getMelodyCheck() ?: return@runBack null
            val list = signalInteractor.getMelodyList()

            return@runBack list.getOrNull(check)
        } ?: return onEmptyError()

        callback?.updateMelodyGroupEnabled(state.isMelody)
        callback?.updateMelodySummary(melodyItem.title)
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

                if (melodyList.isEmpty()) {
                    callback?.showToast(R.string.pref_toast_melody_empty)
                } else {
                    callback?.updateMelodyGroupEnabled(isEnabled = true)
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
                else -> callback?.showToast(R.string.pref_toast_melody_empty)
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