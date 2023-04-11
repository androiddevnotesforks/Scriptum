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
import sgtmelon.scriptum.infrastructure.receiver.action.AlarmActionReceiver
import sgtmelon.scriptum.infrastructure.system.delegators.ToastDelegator
import sgtmelon.test.prod.RunPrivate
import timber.log.Timber

/**
 * Class for help control [AlarmManager]
 */
class AlarmDelegatorImpl(
    private val context: Context,
    private val toast: ToastDelegator?
) : AlarmDelegator {

    private val alarmManager: AlarmManager = context.getAlarmService()

    override fun set(noteId: Long, calendar: Calendar, showToast: Boolean) {
        val intent = AlarmActionReceiver[context, noteId]
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intent)

        if (showToast && toast != null) {
            val date = calendar.formatFuture(context, DateUtils.DAY_IN_MILLIS).lowercase()
            toast.show(context, context.getString(R.string.toast_alarm_set, date))
        }

        if (BuildConfig.DEBUG) {
            Timber.e("ALARM | set intent | hashCode=${intent.hashCode()}")
            intentList.add(intent)
        }
    }

    override fun cancel(id: Long) {
        alarmManager.cancel(AlarmActionReceiver[context, id])
    }

    override fun clear() = with(intentList) {
        if (isEmpty()) return

        forEach {
            Timber.e("ALARM | cancel intent | hashCode=${it.hashCode()}")
            alarmManager.cancel(it)
        }

        clear()
    }

    companion object {
        @RunPrivate val intentList: MutableList<PendingIntent> = mutableListOf()

        private var instance: AlarmDelegator? = null

        operator fun get(context: Context, toast: ToastDelegator?): AlarmDelegator {
            return instance ?: AlarmDelegatorImpl(context, toast).also { instance = it }
        }
    }
}