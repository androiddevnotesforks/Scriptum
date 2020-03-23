package sgtmelon.scriptum.presentation.control.system.callback

import sgtmelon.scriptum.presentation.control.system.PowerControl

/**
 * Interface for communicate with [PowerControl]
 */
interface IPowerControl {

    val isScreenOn: Boolean

    fun acquire(timeout: Long)

    fun release()

}