package sgtmelon.scriptum.control.alarm

import android.content.Context
import android.os.Build
import android.os.PowerManager
import sgtmelon.scriptum.control.alarm.callback.IPowerControl


/**
 * Class for help control [PowerManager]
 */
class PowerControl(context: Context?) : IPowerControl {

    private val powerManager = context?.getSystemService(Context.POWER_SERVICE) as? PowerManager

    private val isScreenOn: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            powerManager?.isInteractive == true
        } else {
            powerManager?.isScreenOn == true
        }

    override fun acquire(timeout: Long) {
        if (isScreenOn) return

        wakeLock = powerManager?.newWakeLock(PowerManager.FULL_WAKE_LOCK or
                PowerManager.ACQUIRE_CAUSES_WAKEUP or
                PowerManager.ON_AFTER_RELEASE, TAG)

        wakeLock?.acquire(timeout)
    }

    override fun release() {
        if (wakeLock != null) {
            wakeLock?.release()
            wakeLock = null
        }
    }

    companion object {
        private const val TAG = "Tag:PowerControl"

        private var wakeLock: PowerManager.WakeLock? = null
    }

}