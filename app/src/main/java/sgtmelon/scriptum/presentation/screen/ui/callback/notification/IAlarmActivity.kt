package sgtmelon.scriptum.presentation.screen.ui.callback.notification

import androidx.annotation.ArrayRes
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.key.ColorShade
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.notification.IAlarmViewModel

/**
 * Interface for communication [IAlarmViewModel] with [AlarmActivity].
 */
interface IAlarmActivity : IAlarmBridge {

    /**
     * Awake phone if it sleeps
     */
    fun acquirePhone(timeout: Long)

    /**
     * Comeback phone to sleep if it need
     */
    fun releasePhone()


    fun setupView()

    fun setupInsets()

    fun setupPlayer(stringUri: String, volume: Int, increase: Boolean)

    fun prepareLogoAnimation()

    fun notifyList(item: NoteItem)


    fun waitLayoutConfigure()

    fun startRippleAnimation(@Theme theme: Int, @Color color: Int, shade: ColorShade)

    fun startButtonFadeInAnimation()

    fun openNoteScreen(item: NoteItem)


    fun startLongWaitHandler(delay: Long, r: Runnable)

    fun startVibratorHandler(delay: Long, r: Runnable)


    fun melodyStart()

    fun melodyStop()

    fun vibrateStart(pattern: LongArray)

    fun vibrateCancel()


    fun showRepeatToast(select: Int)

    fun getIntArray(@ArrayRes arrayId: Int): IntArray

    fun sendUpdateBroadcast(id: Long)

    fun finish()

}