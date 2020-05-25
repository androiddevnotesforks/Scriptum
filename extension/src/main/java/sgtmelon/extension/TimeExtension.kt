package sgtmelon.extension

import android.content.Context
import android.text.format.DateUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.text.format.DateFormat as DateFormatAndroid

// TODO add unit tests
// TODO add getCalendar()

private fun getDateFormat() = SimpleDateFormat(BuildConfig.DATE_FORMAT_DB, Locale.getDefault())

fun Context?.is24Format(): Boolean {
    return if (this != null) DateFormatAndroid.is24HourFormat(this) else true
}

fun getTime(): String = Calendar.getInstance().getText()

fun Calendar.isToday() = DateUtils.isToday(timeInMillis)

fun Calendar.isThisYear() = get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)

fun String.getCalendar(): Calendar = let {
    val calendar = Calendar.getInstance()

    if (it.isEmpty()) return calendar

    try {
        calendar.time = getDateFormat().parse(it)
    } catch (e: Throwable) {
        e.printStackTrace()
    }

    return calendar
}

fun String.getCalendarOrNull(): Calendar? = let {
    if (it.isEmpty()) return null

    return try {
        Calendar.getInstance().apply { time = getDateFormat().parse(it) }
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

fun Calendar.getText(): String = let { getDateFormat().format(it.time) }

fun Calendar.beforeNow() = this.before(Calendar.getInstance())

fun Calendar.afterNow() = this.after(Calendar.getInstance())

fun Calendar.formatFuture(context: Context,
                          maxResolution: Long = DateUtils.WEEK_IN_MILLIS) : String {
    return DateUtils.getRelativeDateTimeString(context, timeInMillis,
            DateUtils.DAY_IN_MILLIS, maxResolution, 0
    ).toString()
}

fun Calendar.clearSeconds() = apply {
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

fun Calendar.formatPast(): String {
    return when {
        isToday() -> DateFormat.getTimeInstance(DateFormat.SHORT).format(time)
        isThisYear() -> SimpleDateFormat(BuildConfig.DATE_FORMAT_DATE_MEDIUM, Locale.getDefault()).format(time)
        else -> DateFormat.getDateInstance(DateFormat.SHORT).format(time)
    }
}

/**
 * TODO Think about this methods. Where you can move them, or make visible only for tests/
 */
fun getRandomFutureTime(): String {
    return Calendar.getInstance().clearSeconds().apply {
        add(Calendar.MINUTE, (1..60).random())
        add(Calendar.HOUR_OF_DAY, (1..12).random())
        add(Calendar.DAY_OF_YEAR, (10..30).random())
    }.getText()
}

fun getRandomPastTime(): String {
    return Calendar.getInstance().clearSeconds().apply {
        add(Calendar.MINUTE, -(1..60).random())
        add(Calendar.HOUR_OF_DAY, -(1..12).random())
        add(Calendar.DAY_OF_YEAR, -(10..30).random())
    }.getText()
}