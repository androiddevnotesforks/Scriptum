package sgtmelon.scriptum.presentation.control.system.callback

import sgtmelon.scriptum.presentation.control.system.AlarmControl
import java.util.*

/**
 * Interface for communicate with [AlarmControl]
 */
interface IAlarmControl {

    fun set(calendar: Calendar, id: Long, showToast: Boolean)

    fun cancel(id: Long)

    /**
     * Clear alarm on test tearDown.
     */
    fun clear()

}