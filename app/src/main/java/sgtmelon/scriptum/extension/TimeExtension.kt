package sgtmelon.scriptum.extension

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import sgtmelon.scriptum.R
import java.text.SimpleDateFormat
import java.util.*

// TODO remove context
fun Context.getDateFormat(): SimpleDateFormat =
        SimpleDateFormat(getString(R.string.date_app_format), Locale.getDefault())

fun Context.is24Format(): Boolean = DateFormat.is24HourFormat(this)

/**
 * Текущее время в нужном формате
 */
fun Context.getTime(): String = getDateFormat().format(Calendar.getInstance().time)

fun Calendar.isToday() = DateUtils.isToday(timeInMillis)

fun Calendar.isThisYear() = get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)

fun String.getCalendar(context: Context): Calendar {
    val calendar = Calendar.getInstance()

    try {
        calendar.time = context.getDateFormat().parse(this)
    } catch (e: Throwable) {
        e.printStackTrace()
    }

    return calendar
}

fun Calendar.beforeNow() = this.before(Calendar.getInstance())

fun Calendar.afterNow() = this.after(Calendar.getInstance())