package sgtmelon.scriptum.infrastructure.system.delegators

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import sgtmelon.scriptum.cleanup.extension.getVibratorService
import sgtmelon.scriptum.infrastructure.screen.DelayJobDelegator

/**
 * Class, which delegates [Vibrator] functions.
 */
class VibratorDelegator(context: Context?) {

    private val manager = context?.getVibratorService()
    private val repeatDelayJob = DelayJobDelegator(lifecycle = null)

    fun startRepeat(pattern: LongArray = defaultPattern) {
        startSingle(pattern)
        repeatDelayJob.run(pattern.sum()) {
            startRepeat(pattern)
        }
    }

    private fun startSingle(pattern: LongArray = defaultPattern) {
        if (manager?.hasVibrator() == false) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibe = VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE)
            manager?.vibrate(vibe)
        } else {
            manager?.vibrate(pattern, -1)
        }
    }

    fun cancel() {
        manager?.cancel()
        repeatDelayJob.cancel()
    }

    companion object {
        val defaultPattern = longArrayOf(500, 750, 500, 750, 500, 0)
    }
}