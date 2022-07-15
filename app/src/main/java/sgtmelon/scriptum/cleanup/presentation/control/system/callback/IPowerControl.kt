package sgtmelon.scriptum.cleanup.presentation.control.system.callback

import sgtmelon.scriptum.cleanup.presentation.control.system.PowerControl

/**
 * Interface for communicate with [PowerControl]
 */
interface IPowerControl {

    val isScreenOn: Boolean

    fun acquire(timeout: Long)

    fun release()

}