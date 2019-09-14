package sgtmelon.scriptum.control.alarm.callback

import sgtmelon.scriptum.control.alarm.PowerControl

/**
 * Interface for communication with [PowerControl]
 */
interface IPowerControl {

    fun acquire(timeout: Long)

    fun release()

}