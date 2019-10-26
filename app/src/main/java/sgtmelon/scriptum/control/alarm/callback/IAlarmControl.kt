package sgtmelon.scriptum.control.alarm.callback

import sgtmelon.scriptum.control.alarm.AlarmControl
import java.util.*

/**
 * Interface for communicate with [AlarmControl]
 */
interface IAlarmControl {

    fun set(calendar: Calendar, id: Long, showToast: Boolean = true)

    fun cancel(id: Long)

}