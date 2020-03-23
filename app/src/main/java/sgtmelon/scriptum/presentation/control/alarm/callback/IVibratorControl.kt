package sgtmelon.scriptum.presentation.control.alarm.callback

import sgtmelon.scriptum.presentation.control.alarm.VibratorControl

/**
 * Interface for communicate with [VibratorControl]
 */
interface IVibratorControl {

    fun start(pattern: LongArray)

    fun cancel()

}