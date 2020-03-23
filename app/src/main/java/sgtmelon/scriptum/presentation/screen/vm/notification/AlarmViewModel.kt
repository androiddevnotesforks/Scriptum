package sgtmelon.scriptum.presentation.screen.vm.notification

import android.app.Application
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.model.state.SignalState
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.presentation.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.presentation.screen.vm.ParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.notification.IAlarmViewModel

/**
 * ViewModel for [AlarmActivity].
 */
class AlarmViewModel(application: Application) : ParentViewModel<IAlarmActivity>(application),
        IAlarmViewModel {

    private lateinit var interactor: IAlarmInteractor
    private lateinit var signalInteractor: ISignalInteractor
    private lateinit var bindInteractor: IBindInteractor

    fun setInteractor(interactor: IAlarmInteractor, signalInteractor: ISignalInteractor,
                      bindInteractor: IBindInteractor) {
        this.interactor = interactor
        this.signalInteractor = signalInteractor
        this.bindInteractor = bindInteractor
    }


    @VisibleForTesting
    var id: Long = NoteData.Default.ID

    @VisibleForTesting
    lateinit var noteItem: NoteItem
    @VisibleForTesting
    var signalState: SignalState? = null

    private val longWaitRunnable = Runnable { repeatFinish() }
    private val vibratorRunnable = object : Runnable {
        override fun run() {
            callback?.vibrateStart(vibratorPattern)
            callback?.startVibratorHandler(vibratorPattern.sum(), r = this)
        }
    }

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            acquirePhone(CANCEL_DELAY)
            setupView(interactor.theme)
            setupPlayer(signalInteractor.melodyUri, interactor.volume, interactor.volumeIncrease)
        }

        if (bundle != null) {
            id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)
        }

        viewModelScope.launch {
            /**
             * If first open.
             */
            if (!::noteItem.isInitialized) {
                interactor.getModel(id)?.let {
                    noteItem = it
                } ?: run {
                    callback?.finish()
                    return@launch
                }

                signalState = signalInteractor.signalState
                bindInteractor.notifyInfoBind(callback)
            }

            callback?.apply {
                notifyList(noteItem)
                waitLayoutConfigure()
            }
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        if (signalState?.isMelody == true) {
            callback?.melodyStop()
        }

        if (signalState?.isVibration == true) {
            callback?.vibrateCancel()
        }

        callback?.releasePhone()
        interactor.onDestroy()
    }


    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putLong(NoteData.Intent.ID, id)
    }

    override fun onStart() {
        val theme = interactor.theme

        callback?.apply {
            startRippleAnimation(theme, noteItem.color, theme.getRippleShade())
            startButtonFadeInAnimation()

            if (signalState?.isMelody == true) {
                melodyStart()
            }

            if (signalState?.isVibration == true) {
                startVibratorHandler(START_DELAY, vibratorRunnable)
            }

            startLongWaitHandler(CANCEL_DELAY, longWaitRunnable)
        }
    }

    private fun Int.getRippleShade(): ColorShade = let {
        return if (it == Theme.LIGHT) ColorShade.ACCENT else ColorShade.DARK
    }

    override fun onClickNote() {
        callback?.startNoteActivity(noteItem)
        callback?.finish()
    }

    override fun onClickDisable() {
        callback?.finish()
    }

    override fun onClickRepeat() = repeatFinish()

    override fun onResultRepeatDialog(@IdRes itemId: Int) {
        repeatFinish(repeat = getRepeatById(itemId) ?: interactor.repeat)
    }

    private fun getRepeatById(@IdRes itemId: Int): Int? = when(itemId) {
        R.id.item_repeat_0 -> Repeat.MIN_10
        R.id.item_repeat_1 -> Repeat.MIN_30
        R.id.item_repeat_2 -> Repeat.MIN_60
        R.id.item_repeat_3 -> Repeat.MIN_180
        R.id.item_repeat_4 -> Repeat.MIN_1440
        else -> null
    }

    /**
     * Call this when need set alarm repeat with screen finish.
     */
    private fun repeatFinish(@Repeat repeat: Int = interactor.repeat) {
        val valueArray = callback?.getIntArray(R.array.pref_alarm_repeat_array) ?: return

        viewModelScope.launch {
            interactor.setupRepeat(noteItem, valueArray, repeat)

            callback?.showRepeatToast(repeat)
            callback?.sendUpdateBroadcast(id)
            callback?.finish()
        }
    }

    /**
     * Calls on note notification cancel from status bar for update bind indicator.
     */
    override fun onReceiveUnbindNote(id: Long) {
        if (this.id != id) return

        callback?.notifyList(noteItem.apply { isStatus = false })
    }


    private val vibratorPattern = longArrayOf(500, 750, 500, 750, 500, 0)

    companion object {
        @VisibleForTesting
        const val START_DELAY = 0L

        @VisibleForTesting
        const val CANCEL_DELAY = 20000L
    }

}