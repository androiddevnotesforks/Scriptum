package sgtmelon.scriptum.infrastructure.system.delegators

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import sgtmelon.extensions.getVibratorService
import sgtmelon.scriptum.infrastructure.utils.DelayedJob

/**
 * Class, which delegates [Vibrator] functions.
 */
class VibratorDelegator(context: Context) {

    private val manager: Vibrator = context.getVibratorService()
    private val repeatDelayJob = DelayedJob(lifecycle = null)

    private var isVibrate = false

    fun startRepeat(pattern: LongArray = defaultPattern) {
        if (!manager.hasVibrator()) return

        startSingle(pattern)
        repeatDelayJob.start(pattern.sum()) {
            startRepeat(pattern)
        }
    }

    private fun startSingle(pattern: LongArray = defaultPattern) {
        if (!manager.hasVibrator()) return

        isVibrate = true

        val vibe = VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE)
        manager.vibrate(vibe)
    }

    fun cancel() {
        if (!isVibrate) return

        isVibrate = false
        manager.cancel()
        repeatDelayJob.cancel()
    }

    companion object {
        val defaultPattern = longArrayOf(500, 750, 500, 750, 500, 0)
    }
}