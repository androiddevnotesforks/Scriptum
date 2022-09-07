package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.notification

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.common.utils.getCalendarWithAdd
import sgtmelon.common.utils.runBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.annotation.test.IdlingTag
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Note.Default
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Note.Intent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.ShiftDateIfExistUseCase
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.test.idling.getIdling

/**
 * ViewModel for [IAlarmActivity].
 */
class AlarmViewModel(
    callback: IAlarmActivity,
    private val preferencesRepo: PreferencesRepo,
    private val noteRepo: NoteRepo,
    private val getMelodyList: GetMelodyListUseCase,
    private val setNotification: SetNotificationUseCase,
    private val deleteNotification: DeleteNotificationUseCase,
    private val shiftDateOnExist: ShiftDateIfExistUseCase
) : ParentViewModel<IAlarmActivity>(callback),
        IAlarmViewModel {

    @RunPrivate var id: Long = Default.ID

    @RunPrivate lateinit var noteItem: NoteItem

    private val longWaitRunnable = Runnable { repeatFinish(preferencesRepo.repeat) }
    private val vibratorRunnable = object : Runnable {
        override fun run() {
            callback.vibrateStart(vibratorPattern)
            callback.startVibratorHandler(vibratorPattern.sum(), r = this)
        }
    }

    override fun onSetup(bundle: Bundle?) {
        id = bundle?.getLong(Intent.ID, Default.ID) ?: Default.ID

        callback?.apply {
            acquirePhone(CANCEL_DELAY)
            setupView()
            setupInsets()
        }

        viewModelScope.launch {
            val melodyUri = runBack { preferencesRepo.getMelodyUri(getMelodyList()) }

            if (melodyUri != null) {
                val volume = preferencesRepo.volume
                val isVolumeIncrease = preferencesRepo.isVolumeIncrease
                callback?.setupPlayer(melodyUri, volume, isVolumeIncrease)
            }

            /** If first open. */
            if (!::noteItem.isInitialized) {
                val item = runBack {
                    /**
                     * Delete before return noteModel. This is need for hide alarm icon and
                     * decrement notification info count (next alarms count).
                     */
                    deleteNotification(id)

                    return@runBack noteRepo.getItem(id, isOptimal = true)
                }

                if (item != null) {
                    noteItem = item
                } else {
                    callback?.finish()
                    return@launch
                }

                callback?.sendNotifyInfoBroadcast()
            }

            callback?.apply {
                prepareLogoAnimation()
                notifyList(noteItem)
            }
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        val signalState = preferencesRepo.signalState

        if (signalState.isMelody) {
            callback?.melodyStop()
        }

        if (signalState.isVibration) {
            callback?.vibrateCancel()
        }

        callback?.releasePhone()
    }


    override fun onSaveData(bundle: Bundle) = with(bundle) { putLong(Intent.ID, id) }

    override fun onStart() {
        val signalState = preferencesRepo.signalState

        getIdling().start(IdlingTag.Alarm.START)

        callback?.apply {
            startRippleAnimation(noteItem.color)
            startButtonFadeInAnimation()

            if (signalState.isMelody) {
                melodyStart()
            }

            if (signalState.isVibration) {
                startVibratorHandler(START_DELAY, vibratorRunnable)
            }

            startLongWaitHandler(CANCEL_DELAY, longWaitRunnable)
        }

        getIdling().stop(IdlingTag.Alarm.START)
    }

    override fun onClickNote() {
        callback?.openNoteScreen(noteItem)
        callback?.finish()
    }

    override fun onClickDisable() {
        callback?.finish()
    }

    override fun onClickRepeat() = repeatFinish(preferencesRepo.repeat)

    override fun onResultRepeatDialog(@IdRes itemId: Int) {
        repeatFinish(repeat = getRepeatById(itemId) ?: preferencesRepo.repeat)
    }

    @RunPrivate fun getRepeatById(@IdRes itemId: Int): Repeat? = when (itemId) {
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
    @RunPrivate fun repeatFinish(repeat: Repeat) {
        val valueArray = callback?.getIntArray(R.array.pref_alarm_repeat_array) ?: return
        val minute = valueArray.getOrNull(repeat.ordinal) ?: return
        val calendar = getCalendarWithAdd(minute)

        viewModelScope.launch {
            runBack {
                shiftDateOnExist(calendar)
                setNotification(noteItem, calendar)
            }

            callback?.sendSetAlarmBroadcast(id, calendar, showToast = false)
            callback?.sendNotifyInfoBroadcast()

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
        @RunPrivate const val START_DELAY = 0L
        @RunPrivate const val CANCEL_DELAY = 20000L
    }
}