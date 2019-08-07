package sgtmelon.scriptum.control.alarm

/**
 * Interface for [VibratorControl]
 *
 * @author SerjantArbuz
 */
interface IVibratorControl {

    fun start(pattern: LongArray)

    fun cancel()

}