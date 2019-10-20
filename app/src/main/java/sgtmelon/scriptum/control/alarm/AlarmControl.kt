package sgtmelon.scriptum.control.alarm

import android.app.AlarmManager
import android.content.Context
import android.text.format.DateUtils
import sgtmelon.extension.formatFuture
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.alarm.callback.IAlarmControl
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.extension.toLowerCase
import sgtmelon.scriptum.receiver.AlarmReceiver
import java.util.*

/**
 * Class for help control [AlarmManager]
 */
class AlarmControl(private val context: Context?) : IAlarmControl {

    private val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    override fun set(calendar: Calendar, model: AlarmReceiver.Model, showToast: Boolean) {
        if (context == null) return

        val intent = AlarmReceiver[context, model]
        alarmManager?.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intent)

        if (showToast) {
            val date = calendar.formatFuture(context, DateUtils.DAY_IN_MILLIS).toLowerCase()
            context.showToast(context.getString(R.string.toast_alarm_set, date))
        }
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