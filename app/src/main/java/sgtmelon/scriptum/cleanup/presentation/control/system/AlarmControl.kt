package sgtmelon.scriptum.cleanup.presentation.control.system

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.text.format.DateUtils
import java.util.Calendar
import sgtmelon.extensions.formatFuture
import sgtmelon.extensions.getAlarmService
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.control.system.callback.IAlarmControl
import sgtmelon.scriptum.infrastructure.receiver.action.AlarmActionReceiver
import sgtmelon.scriptum.infrastructure.system.delegators.ToastDelegator
import sgtmelon.test.prod.RunPrivate

/**
 * Class for help control [AlarmManager]
 */
class AlarmControl(
    private val context: Context?,
    private val toast: ToastDelegator
) : IAlarmControl {

    private val alarmManager = context?.getAlarmService()

    override fun set(calendar: Calendar, id: Long, showToast: Boolean) {
        if (context == null) return

        val intent = AlarmActionReceiver[context, id]
        alarmManager?.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intent)

        if (showToast) {
            val date = calendar.formatFuture(context, DateUtils.DAY_IN_MILLIS).lowercase()
            toast.show(context, context.getString(R.string.toast_alarm_set, date))
        }

        if (BuildConfig.DEBUG) {
            intentList.add(intent)
        }
    }

    override fun cancel(id: Long) {
        if (context == null) return

        alarmManager?.cancel(AlarmActionReceiver[context, id])
    }

    override fun clear() {
        for (it in intentList) {
            alarmManager?.cancel(it)
        }
        intentList.clear()
    }

    companion object {
        @RunPrivate val intentList: MutableList<PendingIntent> = mutableListOf()

        @RunPrivate var instance: IAlarmControl? = null

        operator fun get(context: Context?, toast: ToastDelegator): IAlarmControl {
            return instance ?: AlarmControl(context, toast).also { instance = it }
        }
    }

}