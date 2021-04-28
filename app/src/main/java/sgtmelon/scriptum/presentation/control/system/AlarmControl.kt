package sgtmelon.scriptum.presentation.control.system

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.text.format.DateUtils
import androidx.annotation.MainThread
import sgtmelon.extension.formatFuture
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.extension.getAlarmService
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.extension.toLowerCase
import sgtmelon.scriptum.presentation.control.system.callback.IAlarmControl
import sgtmelon.scriptum.presentation.receiver.action.AlarmActionReceiver
import java.util.*

/**
 * Class for help control [AlarmManager]
 */
class AlarmControl(private val context: Context?) : IAlarmControl {

    private val alarmManager = context?.getAlarmService()

    override fun set(calendar: Calendar, id: Long, showToast: Boolean) {
        if (context == null) return

        val intent = AlarmActionReceiver[context, id]
        alarmManager?.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intent)

        if (showToast) {
            val date = calendar.formatFuture(context, DateUtils.DAY_IN_MILLIS).toLowerCase()
            context.showToast(context.getString(R.string.toast_alarm_set, date))
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
        if (BuildConfig.DEBUG) {
            for (it in intentList) {
                alarmManager?.cancel(it)
            }
            intentList.clear()
        }
    }

    /**
     * Callback which need implement in interface what pass to Interactor
     * It's need to get access [AlarmControl] inside Interactor
     */
    interface Bridge {
        interface Full : Set, Cancel

        interface Set {
            @MainThread fun setAlarm(id: Long, calendar: Calendar, showToast: Boolean = true)
        }

        interface Cancel {
            @MainThread fun cancelAlarm(id: Long)
        }
    }

    companion object {
        @RunPrivate val intentList: MutableList<PendingIntent> = mutableListOf()

        @RunPrivate var instance: IAlarmControl? = null

        operator fun get(context: Context?): IAlarmControl {
            return instance ?: AlarmControl(context).also { instance = it }
        }
    }

}