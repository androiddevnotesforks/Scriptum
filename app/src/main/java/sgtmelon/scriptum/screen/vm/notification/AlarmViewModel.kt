package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.extension.sendTo
import sgtmelon.scriptum.extension.toUri
import sgtmelon.scriptum.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.interactor.notification.AlarmInteractor
import sgtmelon.scriptum.interactor.notification.SignalInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.data.ReceiverData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.model.state.SignalState
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.notification.IAlarmViewModel

/**
 * ViewModel for [AlarmActivity]
 */
class AlarmViewModel(application: Application) : ParentViewModel<IAlarmActivity>(application),
        IAlarmViewModel {

    private val iInteractor: IAlarmInteractor by lazy { AlarmInteractor(context, callback) }
    private val iSignalInteractor: ISignalInteractor = SignalInteractor(context)

    private var id: Long = NoteData.Default.ID

    private var noteItem: NoteItem? = null
    private var signalState: SignalState? = null

    private val vibratorHandler = Handler()
    private val vibratorRunnable = object : Runnable {
        override fun run() {
            callback?.vibrateStart(vibratorPattern)
            vibratorHandler.postDelayed(this, vibratorPattern.sum())
        }
    }

    private val longWaitHandler = Handler()
    private val longWaitRunnable = Runnable { callback?.finish() }

    /**
     * Control setup alarm repeat in [onDestroy]
     */
    private var needRepeat = true

    override fun onSetup(bundle: Bundle?) {
        callback?.apply {
            acquirePhone(CANCEL_DELAY)

            iInteractor.let {
                setupView(it.theme)

                val uri = iSignalInteractor.melodyUri.toUri()
                if (uri != null) {
                    setupPlayer(it.volume, it.volumeIncrease, uri)
                }
            }
        }

        if (bundle != null) {
            id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)
        }

        viewModelScope.launch {
            /**
             * If first open.
             */
            if (noteItem == null) {
                iInteractor.getModel(id)?.let {
                    noteItem = it
                } ?: run {
                    callback?.finish()
                }

                signalState = iSignalInteractor.signalState
            }

            val noteItem = noteItem ?: return@launch

            callback?.apply {
                notifyDataSetChanged(noteItem)
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
            vibratorHandler.removeCallbacksAndMessages(null)
        }

        longWaitHandler.removeCallbacksAndMessages(null)

        if (needRepeat) {
            noteItem?.also {
                val valueArray = context.resources.getIntArray(R.array.value_alarm_repeat_array)
                iInteractor.setupRepeat(it, valueArray)
            }

            callback?.showPostponeToast(iInteractor.repeat)

            context.sendTo(ReceiverData.Filter.MAIN, ReceiverData.Command.UPDATE_ALARM) {
                putExtra(ReceiverData.Values.NOTE_ID, id)
            }
        }

        callback?.releasePhone()

        iInteractor.onDestroy()
    }


    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putLong(NoteData.Intent.ID, id)
    }

    override fun onStart() {
        val color = noteItem?.color ?: return

        callback?.apply {
            val theme = iInteractor.theme
            startRippleAnimation(theme, context.getAppSimpleColor(color,
                    if (theme == Theme.LIGHT) ColorShade.ACCENT else ColorShade.DARK
            ))

            startButtonFadeInAnimation()
        }

        if (signalState?.isMelody == true) {
            callback?.melodyStart()
        }

        if (signalState?.isVibration == true) {
            vibratorHandler.postDelayed(vibratorRunnable, START_DELAY)
        }

        longWaitHandler.postDelayed(longWaitRunnable, CANCEL_DELAY)
    }

    override fun onClickNote() {
        val noteItem = noteItem ?: return

        needRepeat = false

        callback?.apply {
            startNoteActivity(noteItem)
            finish()
        }
    }

    override fun onClickDisable() {
        needRepeat = false
        callback?.finish()
    }

    override fun onClickPostpone() {
        callback?.finish()
    }

    companion object {
        private const val START_DELAY = 0L
        const val CANCEL_DELAY = 20000L

        private val vibratorPattern = longArrayOf(500, 750, 500, 750, 500, 0)
    }

}