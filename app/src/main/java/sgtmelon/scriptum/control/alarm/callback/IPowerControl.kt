package sgtmelon.scriptum.control.alarm.callback

import sgtmelon.scriptum.control.alarm.PowerControl

/**
 * Interface for [PowerControl]
 *
 * @author SerjantArbuz
 */
interface IPowerControl {

    fun acquire(timeout: Long)

    fun release()

}