package sgtmelon.scriptum.control.alarm.callback

import sgtmelon.scriptum.control.alarm.PowerControl

/**
 * Interface for communicate with [PowerControl]
 */
interface IPowerControl {

    val isScreenOn: Boolean

    fun acquire(timeout: Long)

    fun release()

}