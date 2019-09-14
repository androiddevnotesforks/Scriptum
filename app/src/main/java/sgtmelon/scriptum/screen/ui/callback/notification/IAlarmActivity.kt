package sgtmelon.scriptum.screen.ui.callback.notification

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import androidx.annotation.ColorInt
import sgtmelon.scriptum.control.alarm.callback.AlarmCallback
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel
import java.util.*

/**
 * Interface for communication [AlarmViewModel] with [AlarmActivity]
 */
interface IAlarmActivity : AlarmCallback.Set {

    /**
     * TODO Lock phone -> finish alarm
     *
     * Don't use onPause/onResume, because:
     *
     * 1. If phone unlock
     * onResume -> HAPPY
     *
     * 2. If phone lock and don't have password
     * AWAKE -> onResume -> HAPPY
     *
     * 3. If phone lock and have password activity will call
     * AWAKE -> onResume -> onPause -> PASSWORD -> onResume -> HAPPY
     */

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

    fun notifyDataSetChanged(noteModel: NoteModel)

    fun startRippleAnimation(@Theme theme: Int, @ColorInt fillColor: Int)

    fun startButtonFadeInAnimation()

    fun melodyStart()

    fun melodyStop()

    fun vibrateStart(pattern: LongArray)

    fun vibrateCancel()

    fun showPostponeToast(select: Int)

    fun startActivity(intent: Intent)

    fun finish()

}