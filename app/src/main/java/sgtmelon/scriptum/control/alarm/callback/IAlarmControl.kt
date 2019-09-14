package sgtmelon.scriptum.control.alarm.callback

import android.app.PendingIntent
import sgtmelon.scriptum.control.alarm.AlarmControl
import java.util.*

/**
 * Interface for comminication with [AlarmControl]
 */
interface IAlarmControl {

    fun set(calendar: Calendar, intent: PendingIntent)

    fun cancel(intent: PendingIntent)

}