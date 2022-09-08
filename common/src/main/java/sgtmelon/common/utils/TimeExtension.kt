@file:JvmName(name = "TimeExtensionUtils")

package sgtmelon.common.utils

import android.content.Context
import android.text.format.DateUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import sgtmelon.common.BuildConfig
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

private fun getDateFormat() = SimpleDateFormat(BuildConfig.DATE_FORMAT_DB, Locale.getDefault())

fun Context?.is24Format(): Boolean {
    return if (this != null) DateFormatAndroid.is24HourFormat(this) else true
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

    val calendar = sgtmelon.common.utils.getCalendar()

    try {
        getDateFormat().parse(this)?.let { calendar.time = it }
    } catch (e: Throwable) {
        e.printStackTrace()
        return null
    }

    return calendar
}

fun Calendar.toText(): String = getDateFormat().format(this.time)

//endregion

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
            BuildConfig.DATE_FORMAT_DATE_MEDIUM,
            Locale.getDefault()
        ).format(time)
        else -> DateFormat.getDateInstance(DateFormat.SHORT).format(time)
    }
}

private fun Calendar.isToday() = DateUtils.isToday(timeInMillis)

private fun Calendar.isThisYear() = get(Calendar.YEAR) == getCalendar().get(Calendar.YEAR)

//endregion

//region ONLY TESTS

/**
 * TODO Think about this methods. Where you can move them, or make visible only for tests/
 */
fun getRandomFutureTime(): String {
    return getClearCalendar().apply {
        add(Calendar.MINUTE, (1..60).random())
        add(Calendar.HOUR_OF_DAY, (1..12).random())
        add(Calendar.DAY_OF_YEAR, (10..30).random())
    }.toText()
}

fun getRandomPastTime(): String {
    return getClearCalendar().apply {
        add(Calendar.MINUTE, -(1..60).random())
        add(Calendar.HOUR_OF_DAY, -(1..12).random())
        add(Calendar.DAY_OF_YEAR, -(10..30).random())
    }.toText()
}

//endregion