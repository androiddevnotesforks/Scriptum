package sgtmelon.scriptum.control.alarm.callback

import sgtmelon.scriptum.control.alarm.PowerControl

/**
 * Interface for comminication with [PowerControl]
 */
interface IPowerControl {

    fun acquire(timeout: Long)

    fun release()

}