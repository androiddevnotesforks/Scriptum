package sgtmelon.scriptum.control.alarm

import android.app.PendingIntent
import java.util.*

/**
 * Interface for [AlarmControl]
 *
 * @author SerjantArbuz
 */
interface IAlarmControl {

    fun set(calendar: Calendar, intent: PendingIntent)

    fun cancel(intent: PendingIntent)

}