package sgtmelon.scriptum.control.alarm

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import sgtmelon.scriptum.control.alarm.callback.IVibratorControl

/**
 * Class for help control [Vibrator]
 */
class VibratorControl(context: Context?) : IVibratorControl {

    private val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

    override fun start(pattern: LongArray) {
        if (vibrator?.hasVibrator() == false) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createWaveform(
                    pattern, VibrationEffect.DEFAULT_AMPLITUDE
            ))
        } else {
            vibrator?.vibrate(pattern, -1)
        }
    }

    override fun cancel() {
        vibrator?.cancel()
    }

}