package sgtmelon.scriptum.control.alarm.callback

import android.app.PendingIntent
import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.receiver.AlarmReceiver
import java.util.*

/**
 * Interface for communicate with [AlarmControl]
 */
interface IAlarmControl {

    fun set(calendar: Calendar, model: AlarmReceiver.Model)

    fun cancel(model: AlarmReceiver.Model)

}