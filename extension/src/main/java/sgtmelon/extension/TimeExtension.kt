package sgtmelon.extension

import android.content.Context
import android.text.format.DateFormat
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

const val dateAppFormat = "yyyy-MM-dd HH:mm:ss"

fun getDateFormat(): SimpleDateFormat = SimpleDateFormat(dateAppFormat, Locale.getDefault())

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
        calendar.time = getDateFormat().parse(this)
    } catch (e: Throwable) {
        e.printStackTrace()
    }

    return calendar
}

fun Calendar.beforeNow() = this.before(Calendar.getInstance())

fun Calendar.afterNow() = this.after(Calendar.getInstance())