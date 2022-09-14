package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.notification

import androidx.annotation.ArrayRes
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.Repeat

/**
 * Interface for communication [IAlarmViewModel] with [AlarmActivity].
 */
interface IAlarmActivity : SystemReceiver.Bridge.Alarm,
    SystemReceiver.Bridge.Bind {

    fun setupView()

    fun setupInsets()

    fun setupPlayer(stringUri: String, volumePercent: Int, isIncrease: Boolean)

    fun prepareLogoAnimation()

    fun notifyList(item: NoteItem)


    fun startRippleAnimation(color: Color)

    fun startButtonFadeInAnimation()

    fun openNoteScreen(item: NoteItem)


    fun wakePhone(timeout: Long)

    fun releasePhone()

    fun startFinishTimer(time: Long)

    fun startMelody(isIncrease: Boolean)

    fun stopMelody()

    fun startVibrator()

    fun cancelVibrator()


    fun showRepeatToast(repeat: Repeat)

    fun getIntArray(@ArrayRes arrayId: Int): IntArray

    fun sendUpdateBroadcast(id: Long)

    fun finish()

}