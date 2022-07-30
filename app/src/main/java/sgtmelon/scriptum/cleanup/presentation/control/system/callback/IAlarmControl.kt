package sgtmelon.scriptum.cleanup.presentation.control.system.callback

import java.util.Calendar
import sgtmelon.scriptum.cleanup.presentation.control.system.AlarmControl

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