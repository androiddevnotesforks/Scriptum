package sgtmelon.scriptum.control.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import sgtmelon.scriptum.control.alarm.callback.IAlarmControl
import sgtmelon.scriptum.receiver.AlarmReceiver
import java.util.*

/**
 * Class for help control [AlarmManager]
 */
class AlarmControl(private val context: Context?) : IAlarmControl {

    // TODO #RELEASE2 coroutine

    private val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    // TODO remove
    override fun set(calendar: Calendar, intent: PendingIntent) {
        alarmManager?.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intent)
    }

    override fun set(calendar: Calendar, model: AlarmReceiver.Model) {
        if (context == null) return

        val intent = AlarmReceiver[context, model]
        alarmManager?.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intent)
    }

    // TODO remove
    override fun cancel(intent: PendingIntent) {
        alarmManager?.cancel(intent)
    }

    override fun cancel(model: AlarmReceiver.Model) {
        if (context == null) return

        alarmManager?.cancel(AlarmReceiver[context, model])
    }

    companion object {
        private var callback: IAlarmControl? = null

        operator fun get(context: Context?): IAlarmControl =
                callback ?: AlarmControl(context).also { callback = it }
    }

}