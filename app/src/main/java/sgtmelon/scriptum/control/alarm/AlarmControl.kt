package sgtmelon.scriptum.control.alarm

import android.app.AlarmManager
import android.content.Context
import sgtmelon.scriptum.control.alarm.callback.IAlarmControl
import sgtmelon.scriptum.receiver.AlarmReceiver
import java.util.*

/**
 * Class for help control [AlarmManager]
 */
class AlarmControl(private val context: Context?) : IAlarmControl {

    private val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    override fun set(calendar: Calendar, model: AlarmReceiver.Model) {
        if (context == null) return

        val intent = AlarmReceiver[context, model]
        alarmManager?.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intent)
    }

    override fun cancel(model: AlarmReceiver.Model) {
        if (context == null) return

        alarmManager?.cancel(AlarmReceiver[context, model])
    }


    /**
     * Callback which need implement in interface what pass to Interactor
     * It's need to get access [AlarmControl] inside Interactor
     */
    interface Bridge {
        interface Full : Set, Cancel

        interface Set {
            fun setAlarm(calendar: Calendar, model: AlarmReceiver.Model)
        }

        interface Cancel {
            fun cancelAlarm(model: AlarmReceiver.Model)
        }
    }

    companion object {
        private var callback: IAlarmControl? = null

        operator fun get(context: Context?): IAlarmControl =
                callback ?: AlarmControl(context).also { callback = it }
    }

}