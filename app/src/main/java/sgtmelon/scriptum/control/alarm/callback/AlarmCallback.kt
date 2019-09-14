package sgtmelon.scriptum.control.alarm.callback

import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.receiver.AlarmReceiver

/**
 * Callback which need implement in interface what pass to ViewModel.
 * It's need to get access [AlarmControl] inside Interactor
 */
interface AlarmCallback {

    interface Set {
        fun setAlarm(model: AlarmReceiver.Model)
    }

    interface Cancel {
        fun cancelAlarm(model: AlarmReceiver.Model)
    }

}