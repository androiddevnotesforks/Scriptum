package sgtmelon.scriptum.infrastructure.system.delegators

import android.content.Context
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import sgtmelon.extensions.getPowerService
import sgtmelon.extensions.getVibratorService
import sgtmelon.scriptum.infrastructure.utils.DelayedJob

/**
 * Class, which delegates [Vibrator] functions.
 */
class VibratorDelegator(context: Context) {

    private val manager: Vibrator = context.getVibratorService()
    private val powerManager: PowerManager = context.getPowerService()

    private val repeatDelayJob = DelayedJob(lifecycle = null)

    private var isVibrate = false

    fun startRepeat(pattern: LongArray = repeatPattern) {
        if (!manager.hasVibrator()) return

        makeRepeatPiece(pattern)
        repeatDelayJob.start(pattern.sum()) {
            startRepeat(pattern)
        }
    }

    private fun makeRepeatPiece(pattern: LongArray = repeatPattern) {
        if (!manager.hasVibrator()) return

        isVibrate = true

        val vibe = VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE)
        manager.vibrate(vibe)
    }

    fun cancelRepeat() {
        if (!isVibrate) return

        isVibrate = false
        manager.cancel()
        repeatDelayJob.cancel()
    }

    fun startShort(pattern: LongArray = shortPattern) {
        if (!manager.hasVibrator() || powerManager.isPowerSaveMode) return

        val vibe = VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE)
        manager.vibrate(vibe)
    }

    companion object {
        val repeatPattern = longArrayOf(500, 750, 500, 750, 500, 0)
        val shortPattern = longArrayOf(0, 15, 0)
    }
}