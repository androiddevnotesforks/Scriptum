package sgtmelon.extension

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

// TODO #RELEASE2 объединить с форматированием строки (посмотреть нужно ли всезде)
fun getDateFormat(): SimpleDateFormat =
        SimpleDateFormat(BuildConfig.APP_DATE_FORMAT, Locale.getDefault())

fun Context?.is24Format(): Boolean =
        if (this != null) DateFormat.is24HourFormat(this) else true

/**
 * Текущее время в нужном формате
 */
fun getTime(): String = getDateFormat().format(Calendar.getInstance().time)

fun Calendar.isToday() = DateUtils.isToday(timeInMillis)

fun Calendar.isThisYear() = get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)

fun String.getCalendar(): Calendar {
    val calendar = Calendar.getInstance()

    try {
        calendar.time = getDateFormat().parse(this)
    } catch (e: Throwable) {
        e.printStackTrace()
    }

    return calendar
}

fun Calendar.beforeNow() = this.before(Calendar.getInstance())

fun Calendar.afterNow() = this.after(Calendar.getInstance())

fun Context.getShortAnimTime(): Long =
        resources.getInteger(android.R.integer.config_shortAnimTime).toLong()