package sgtmelon.scriptum.presentation.control.alarm.callback

import sgtmelon.scriptum.presentation.control.alarm.PowerControl

/**
 * Interface for communicate with [PowerControl]
 */
interface IPowerControl {

    val isScreenOn: Boolean

    fun acquire(timeout: Long)

    fun release()

}