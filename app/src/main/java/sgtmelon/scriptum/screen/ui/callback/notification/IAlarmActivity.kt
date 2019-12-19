package sgtmelon.scriptum.screen.ui.callback.notification

import android.net.Uri
import androidx.annotation.ColorInt
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel

/**
 * Interface for communication [AlarmViewModel] with [AlarmActivity]
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


    fun setupView(@Theme theme: Int)

    fun setupPlayer(volume: Int, increase: Boolean, uri: Uri)

    fun notifyList(item: NoteItem)


    fun waitLayoutConfigure()

    fun startRippleAnimation(@Theme theme: Int, @ColorInt fillColor: Int)

    fun startButtonFadeInAnimation()

    fun startNoteActivity(item: NoteItem)


    fun melodyStart()

    fun melodyStop()

    fun vibrateStart(pattern: LongArray)

    fun vibrateCancel()

    fun showRepeatToast(select: Int)

    fun finish()

}