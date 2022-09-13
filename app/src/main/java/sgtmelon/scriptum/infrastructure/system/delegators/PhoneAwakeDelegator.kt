package sgtmelon.scriptum.infrastructure.system.delegators

import android.content.Context
import android.os.PowerManager
import sgtmelon.scriptum.cleanup.extension.getPowerService

/**
 * Class, which delegate work of [powerManager].
 */
class PhoneAwakeDelegator(context: Context?) {

    private val powerManager = context?.getPowerService()

    private var wakeLock: PowerManager.WakeLock? = null

    val isAwake: Boolean get() = powerManager?.isInteractive == true

    fun wakeUp(timeout: Long) {
        if (isAwake) return

        val flags = PowerManager.FULL_WAKE_LOCK or
                PowerManager.ACQUIRE_CAUSES_WAKEUP or
                PowerManager.ON_AFTER_RELEASE

        wakeLock = powerManager?.newWakeLock(flags, TAG)
        wakeLock?.acquire(timeout)
    }

    fun release() {
        wakeLock?.release()
        wakeLock = null
    }

    companion object {
        private const val TAG = "Tag:PowerControl"
    }
}