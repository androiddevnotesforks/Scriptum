package sgtmelon.scriptum.presentation.control.system.callback

import sgtmelon.scriptum.presentation.control.system.VibratorControl

/**
 * Interface for communicate with [VibratorControl]
 */
interface IVibratorControl {

    fun start(pattern: LongArray)

    fun cancel()

}