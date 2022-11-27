package sgtmelon.scriptum.cleanup.presentation.control.system.callback

import java.util.Calendar
import sgtmelon.scriptum.cleanup.presentation.control.system.AlarmDelegator

/**
 * Interface for communicate with [AlarmDelegator]
 */
interface IAlarmDelegator {

    fun set(noteId: Long, calendar: Calendar, showToast: Boolean)

    fun cancel(id: Long)

    /**
     * Clear alarm on test tearDown.
     */
    fun clear()

}