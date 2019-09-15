package sgtmelon.scriptum.control.alarm.callback

import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.receiver.AlarmReceiver
import java.util.*

/**
 * Callback which need implement in interface what pass to ViewModel
 * It's need to get access [AlarmControl] inside Interactor
 */
interface IAlarmBridge {

    interface Set {
        fun setAlarm(calendar: Calendar, model: AlarmReceiver.Model)
    }

    interface Cancel {
        fun cancelAlarm(model: AlarmReceiver.Model)
    }

}