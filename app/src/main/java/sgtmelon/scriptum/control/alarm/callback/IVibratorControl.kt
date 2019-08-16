package sgtmelon.scriptum.control.alarm.callback

import sgtmelon.scriptum.control.alarm.VibratorControl

/**
 * Interface for [VibratorControl]
 *
 * @author SerjantArbuz
 */
interface IVibratorControl {

    fun start(pattern: LongArray)

    fun cancel()

}