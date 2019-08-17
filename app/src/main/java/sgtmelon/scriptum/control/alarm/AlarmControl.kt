package sgtmelon.scriptum.control.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import sgtmelon.scriptum.control.alarm.callback.IAlarmControl
import java.util.*

/**
 * Class for help control [AlarmManager]
 *
 * @author SerjantArbuz
 */
class AlarmControl(context: Context?) : IAlarmControl {

    private val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    override fun set(calendar: Calendar, intent: PendingIntent) {
        alarmManager?.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intent)
    }

    override fun cancel(intent: PendingIntent) {
        alarmManager?.cancel(intent)
    }


    /**
     * Implement this callback in activity/fragment to access [AlarmControl] from viewModel
     */
    interface Callback : SetCallback, CancelCallback

    interface SetCallback {
        fun setAlarm(calendar: Calendar, intent: PendingIntent)
    }

    interface CancelCallback {
        fun cancelAlarm(intent: PendingIntent)
    }


    companion object {
        fun getInstance(context: Context?): IAlarmControl = AlarmControl(context)
    }

}