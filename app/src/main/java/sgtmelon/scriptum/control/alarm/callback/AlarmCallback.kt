package sgtmelon.scriptum.control.alarm.callback

import android.app.PendingIntent
import sgtmelon.scriptum.control.alarm.AlarmControl
import java.util.*

/**
 * Callback which need implement in interface what pass to ViewModel.
 * It's need to get access [AlarmControl] inside Interactor
 */
interface AlarmCallback {

    interface Set {
        fun setAlarm(calendar: Calendar, intent: PendingIntent)
    }

    interface Cancel {
        fun cancelAlarm(intent: PendingIntent)
    }

}