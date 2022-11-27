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
import sgtmelon.scriptum.cleanup.presentation.control.system.callback.IAlarmDelegator
import sgtmelon.scriptum.infrastructure.receiver.action.AlarmActionReceiver
import sgtmelon.scriptum.infrastructure.system.delegators.ToastDelegator
import sgtmelon.test.prod.RunPrivate

/**
 * Class for help control [AlarmManager]
 */
class AlarmDelegator(
    private val context: Context,
    private val toast: ToastDelegator
) : IAlarmDelegator {

    private val alarmManager: AlarmManager = context.getAlarmService()

    override fun set(noteId: Long, calendar: Calendar, showToast: Boolean) {
        val intent = AlarmActionReceiver[context, noteId]
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intent)

        if (showToast) {
            val date = calendar.formatFuture(context, DateUtils.DAY_IN_MILLIS).lowercase()
            toast.show(context, context.getString(R.string.toast_alarm_set, date))
        }

        if (BuildConfig.DEBUG) {
            intentList.add(intent)
        }
    }

    override fun cancel(id: Long) {
        alarmManager.cancel(AlarmActionReceiver[context, id])
    }

    override fun clear() {
        for (it in intentList) {
            alarmManager.cancel(it)
        }
        intentList.clear()
    }

    companion object {
        @RunPrivate val intentList: MutableList<PendingIntent> = mutableListOf()

        @RunPrivate var instance: IAlarmDelegator? = null

        operator fun get(context: Context, toast: ToastDelegator): IAlarmDelegator {
            return instance ?: AlarmDelegator(context, toast).also { instance = it }
        }
    }
}