package sgtmelon.scriptum.presentation.control.system

import android.content.Context
import android.os.PowerManager
import sgtmelon.scriptum.presentation.control.system.callback.IPowerControl

/**
 * Class for help control [PowerManager]
 */
class PowerControl(context: Context?) : IPowerControl {

    private val powerManager = context?.getSystemService(Context.POWER_SERVICE) as? PowerManager

    override val isScreenOn: Boolean get() = powerManager?.isInteractive == true

    override fun acquire(timeout: Long) {
        if (isScreenOn) return

        val flags = PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or
                PowerManager.ON_AFTER_RELEASE

        wakeLock = powerManager?.newWakeLock(flags, TAG)

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