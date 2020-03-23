package sgtmelon.scriptum.presentation.control.alarm.callback

import sgtmelon.scriptum.presentation.control.alarm.AlarmControl
import java.util.*

/**
 * Interface for communicate with [AlarmControl]
 */
interface IAlarmControl {

    fun set(calendar: Calendar, id: Long, showToast: Boolean = true)

    fun cancel(id: Long)

    /**
     * Clear alarm on test tearDown.
     */
    fun clear()

}