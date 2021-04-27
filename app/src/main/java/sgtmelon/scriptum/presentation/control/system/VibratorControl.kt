package sgtmelon.scriptum.presentation.control.system

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import sgtmelon.scriptum.extension.getVibratorService
import sgtmelon.scriptum.presentation.control.system.callback.IVibratorControl

/**
 * Class for help control [Vibrator]
 */
class VibratorControl(context: Context?) : IVibratorControl {

    private val manager = context?.getVibratorService()

    override fun start(pattern: LongArray) {
        if (manager?.hasVibrator() == false) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager?.vibrate(VibrationEffect.createWaveform(
                pattern, VibrationEffect.DEFAULT_AMPLITUDE
            ))
        } else {
            manager?.vibrate(pattern, -1)
        }
    }

    override fun cancel() {
        manager?.cancel()
    }

}