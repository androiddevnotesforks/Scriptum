package sgtmelon.scriptum.cleanup.presentation.control.system

import java.util.Calendar

/**
 * Interface for communicate with [AlarmDelegatorImpl]
 */
interface AlarmDelegator {

    fun set(noteId: Long, calendar: Calendar, showToast: Boolean)

    fun cancel(id: Long)

    /**
     * Clear alarm on test tearDown.
     */
    fun clear()

}