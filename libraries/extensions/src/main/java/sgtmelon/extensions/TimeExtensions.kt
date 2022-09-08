@file:JvmName(name = "TimeExtensionsUtils")

package sgtmelon.extensions

import android.content.Context
import android.text.format.DateUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.text.format.DateFormat as DateFormatAndroid

// TODO add unit tests
// TODO make all access to calendar - suspendable

fun getCalendar(): Calendar = Calendar.getInstance()

/**
 * Calendar of current time but with clear millis/seconds (which equals 0/0).
 */
fun getClearCalendar(): Calendar {
    return getCalendar().clearSeconds()
}

fun Calendar.clearSeconds() = apply {
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

fun getClearCalendar(addMinutes: Int): Calendar {
    return getClearCalendar().apply {
        add(Calendar.MINUTE, addMinutes)
    }
}

fun getCalendarText(): String = getCalendar().toText()

//region Converting

fun String.toCalendar(): Calendar {
    val calendar = getCalendar()

    if (isEmpty()) return calendar

    try {
        getDateFormat().parse(this)?.let { calendar.time = it }
    } catch (e: Throwable) {
        e.printStackTrace()
    }

    return calendar
}

fun String.toCalendarOrNull(): Calendar? {
    if (isEmpty()) return null

    val calendar = getCalendar()

    try {
        getDateFormat().parse(this)?.let { calendar.time = it }
    } catch (e: Throwable) {
        e.printStackTrace()
        return null
    }

    return calendar
}

fun Calendar.toText(): String = getDateFormat().format(this.time)

private fun getDateFormat() = SimpleDateFormat(BuildConfig.DATE_FORMAT_FULL, Locale.getDefault())

//endregion

fun Context.is24HourFormat(): Boolean = DateFormatAndroid.is24HourFormat(this)

fun Calendar.isBeforeNow() = this.before(getCalendar())

fun Calendar.isAfterNow() = this.after(getCalendar())

//region Formatting

fun Calendar.formatFuture(
    context: Context,
    maxResolution: Long = DateUtils.WEEK_IN_MILLIS
): String {
    return DateUtils.getRelativeDateTimeString(
        context, timeInMillis,
        DateUtils.DAY_IN_MILLIS, maxResolution, 0
    ).toString()
}

fun Calendar.formatPast(): String {
    return when {
        isToday() -> DateFormat.getTimeInstance(DateFormat.SHORT).format(time)
        isThisYear() -> SimpleDateFormat(
            BuildConfig.DATE_FORMAT_SHORT,
            Locale.getDefault()
        ).format(time)
        else -> DateFormat.getDateInstance(DateFormat.SHORT).format(time)
    }
}

private fun Calendar.isToday() = DateUtils.isToday(timeInMillis)

private fun Calendar.isThisYear() = get(Calendar.YEAR) == getCalendar().get(Calendar.YEAR)

//endregion