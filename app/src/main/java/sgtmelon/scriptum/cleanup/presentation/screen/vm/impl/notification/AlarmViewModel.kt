package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.notification

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.annotation.test.IdlingTag
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.ShiftDateIfExistUseCase
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Intent
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.test.idling.getIdling
import sgtmelon.test.prod.RunPrivate

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
    private val shiftDateIfExist: ShiftDateIfExistUseCase
) : ParentViewModel<IAlarmActivity>(callback),
        IAlarmViewModel {

    @RunPrivate var id: Long = Default.ID

    @RunPrivate lateinit var noteItem: NoteItem

    override fun onSetup(bundle: Bundle?) {
        id = bundle?.getLong(Intent.ID, Default.ID) ?: Default.ID

        callback?.apply {
            wakePhone(FINISH_TIME)
            setupView()
            setupInsets()
        }

        viewModelScope.launch {
            val melodyUri = runBack { preferencesRepo.getMelodyUri(getMelodyList()) }

            if (melodyUri != null) {
                val volumePercent = preferencesRepo.volumePercent
                val isVolumeIncrease = preferencesRepo.isVolumeIncrease
                callback?.setupPlayer(melodyUri, volumePercent, isVolumeIncrease)
            }

            /** If first open. */
            if (!::noteItem.isInitialized) {
                val item = runBack {
                    /**
                     * Delete before return noteModel. This is need for hide alarm icon and
                     * decrement notification info count (next alarms count).
                     */
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

    override fun onSaveData(bundle: Bundle) = with(bundle) { putLong(Intent.ID, id) }

    override fun onStart() {
        val signalState = preferencesRepo.signalState

        getIdling().start(IdlingTag.Alarm.START)

        callback?.apply {
            startRippleAnimation(noteItem.color)
            startButtonFadeInAnimation()

            if (signalState.isMelody) {
                startMelody(preferencesRepo.isVolumeIncrease)
            }

            if (signalState.isVibration) {
                callback?.startVibrator()
            }

            startFinishTimer(FINISH_TIME)
        }

        getIdling().stop(IdlingTag.Alarm.START)
    }

    override fun onClickDisable() {
        callback?.finish()
    }

    override fun onClickRepeat() = finishWithRepeat()

    override fun onResultRepeatDialog(@IdRes itemId: Int) = finishWithRepeat(getRepeatById(itemId))

    @RunPrivate fun getRepeatById(@IdRes itemId: Int): Repeat? = when (itemId) {
        R.id.item_repeat_0 -> Repeat.MIN_10
        R.id.item_repeat_1 -> Repeat.MIN_30
        R.id.item_repeat_2 -> Repeat.MIN_60
        R.id.item_repeat_3 -> Repeat.MIN_180
        R.id.item_repeat_4 -> Repeat.MIN_1440
        else -> null
    }

    /**
     * Call this when need set alarm repeat with screen finish. If [repeat] is null when will
     * use value saved preferences.
     */
    override fun finishWithRepeat(repeat: Repeat?) {
        val actualRepeat = repeat ?: preferencesRepo.repeat

        val valueArray = callback?.getIntArray(R.array.pref_alarm_repeat_array) ?: return
        val minute = valueArray.getOrNull(actualRepeat.ordinal) ?: return
        val calendar = getClearCalendar(minute)

        viewModelScope.launch {
            runBack {
                shiftDateIfExist(calendar)
                setNotification(noteItem, calendar)
            }

            callback?.sendSetAlarmBroadcast(id, calendar, showToast = false)
            callback?.sendNotifyInfoBroadcast()

            callback?.showRepeatToast(actualRepeat)
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

    companion object {
        @RunPrivate const val FINISH_TIME = 20000L
    }
}