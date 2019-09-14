package sgtmelon.scriptum.control.alarm.callback

import android.app.PendingIntent
import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.receiver.AlarmReceiver
import java.util.*

/**
 * Interface for communication with [AlarmControl]
 */
interface IAlarmControl {

    // TODO remove
    fun set(calendar: Calendar, intent: PendingIntent)

    fun set(calendar: Calendar, model: AlarmReceiver.Model)

    // TODO remove
    fun cancel(intent: PendingIntent)

    fun cancel(model: AlarmReceiver.Model)

}