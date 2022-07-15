package sgtmelon.scriptum.cleanup.presentation.control.system.callback

import sgtmelon.scriptum.cleanup.presentation.control.system.VibratorControl

/**
 * Interface for communicate with [VibratorControl]
 */
interface IVibratorControl {

    fun start(pattern: LongArray)

    fun cancel()

}