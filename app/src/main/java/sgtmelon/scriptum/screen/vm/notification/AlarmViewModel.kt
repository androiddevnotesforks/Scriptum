package sgtmelon.scriptum.screen.vm.notification

import android.app.Application
import android.media.RingtoneManager
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.extension.sendTo
import sgtmelon.scriptum.extension.toUri
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.interactor.notification.AlarmInteractor
import sgtmelon.scriptum.interactor.notification.SignalInteractor
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.data.ReceiverData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.model.state.SignalState
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.repository.room.AlarmRepo
import sgtmelon.scriptum.repository.room.BindRepo
import sgtmelon.scriptum.repository.room.NoteRepo
import sgtmelon.scriptum.repository.room.RankRepo
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.ParentViewModel
import sgtmelon.scriptum.screen.vm.callback.notification.IAlarmViewModel

/**
 * ViewModel for [AlarmActivity].
 */
class AlarmViewModel(application: Application) : ParentViewModel<IAlarmActivity>(application),
        IAlarmViewModel {

    private val iInteractor: IAlarmInteractor by lazy {
        AlarmInteractor(PreferenceRepo(context), AlarmRepo(context), NoteRepo(context), callback)
    }
    private val iSignalInteractor: ISignalInteractor by lazy {
        SignalInteractor(PreferenceRepo(context), RingtoneManager(context))
    }
    private val iBindInteractor: IBindInteractor by lazy {
        BindInteractor(
                PreferenceRepo(context), BindRepo(context), RankRepo(context), NoteRepo(context)
        )
    }

    private var id: Long = NoteData.Default.ID

    private lateinit var noteItem: NoteItem
    private var signalState: SignalState? = null

    private val vibratorHandler = Handler()
    private val vibratorRunnable = object : Runnable {
        override fun run() {
            callback?.vibrateStart(vibratorPattern)
            vibratorHandler.postDelayed(this, vibratorPattern.sum())
        }
    }

    private val longWaitHandler = Handler()
    private val longWaitRunnable = Runnable { repeatFinish() }

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
            if (!::noteItem.isInitialized) {
                iInteractor.getModel(id)?.let {
                    noteItem = it
                } ?: run {
                    callback?.finish()
                    return@launch
                }

                signalState = iSignalInteractor.signalState

                iBindInteractor.notifyInfoBind(callback)
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
            vibratorHandler.removeCallbacksAndMessages(null)
        }

        longWaitHandler.removeCallbacksAndMessages(null)

        callback?.releasePhone()

        iInteractor.onDestroy()
    }


    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putLong(NoteData.Intent.ID, id)
    }

    override fun onStart() {
        val color = noteItem.color

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
        callback?.apply {
            startNoteActivity(noteItem)
            finish()
        }
    }

    override fun onClickDisable() {
        callback?.finish()
    }

    override fun onClickRepeat() = repeatFinish()

    override fun onResultRepeatDialog(menuItem: MenuItem) {
        val repeat = getRepeatById(menuItem.itemId) ?: iInteractor.repeat
        repeatFinish(repeat)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getRepeatById(@IdRes itemId: Int): Int? = when(itemId) {
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
    private fun repeatFinish(@Repeat repeat: Int = iInteractor.repeat) {
        viewModelScope.launch {
            val valueArray = context.resources.getIntArray(R.array.pref_alarm_repeat_array)
            iInteractor.setupRepeat(noteItem, valueArray, repeat)

            callback?.showRepeatToast(repeat)

            context.sendTo(ReceiverData.Filter.MAIN, ReceiverData.Command.UPDATE_ALARM) {
                putExtra(ReceiverData.Values.NOTE_ID, id)
            }

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


    companion object {
        private const val START_DELAY = 0L
        const val CANCEL_DELAY = 20000L

        private val vibratorPattern = longArrayOf(500, 750, 500, 750, 500, 0)
    }

}