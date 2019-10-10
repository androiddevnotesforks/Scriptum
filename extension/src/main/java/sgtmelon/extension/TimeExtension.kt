package sgtmelon.extension

import android.content.Context
import android.text.format.DateUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.text.format.DateFormat as DateFormatAndroid

// TODO #RELEASE2 объединить с форматированием строки (посмотреть нужно ли всезде)
fun getDateFormat(): SimpleDateFormat {
    return SimpleDateFormat(BuildConfig.DATE_FORMAT_DB, Locale.getDefault())
}

fun Context?.is24Format(): Boolean {
    return if (this != null) DateFormatAndroid.is24HourFormat(this) else true
}

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

fun Calendar.formatFuture(context: Context,
                          maxResolution: Long = DateUtils.WEEK_IN_MILLIS) : String {
    return DateUtils.getRelativeDateTimeString(context, timeInMillis,
            DateUtils.DAY_IN_MILLIS, maxResolution, 0
    ).toString()
}

fun Calendar.formatPast(): String {
    return when {
        isToday() -> DateFormat.getTimeInstance(DateFormat.SHORT).format(time)
        isThisYear() -> SimpleDateFormat(BuildConfig.DATE_FORMAT_DATE_MEDIUM, Locale.getDefault()).format(time)
        else -> DateFormat.getDateInstance(DateFormat.SHORT).format(time)
    }
}