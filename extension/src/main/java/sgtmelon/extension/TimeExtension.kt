package sgtmelon.extension

import android.content.Context
import android.text.format.DateUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.text.format.DateFormat as DateFormatAndroid

private fun getDateFormat() = SimpleDateFormat(BuildConfig.DATE_FORMAT_DB, Locale.getDefault())

fun Context?.is24Format(): Boolean {
    return if (this != null) DateFormatAndroid.is24HourFormat(this) else true
}

fun getTime(): String = Calendar.getInstance().getString()

fun Calendar.isToday() = DateUtils.isToday(timeInMillis)

fun Calendar.isThisYear() = get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)

fun String.getCalendar(): Calendar = let {
    val calendar = Calendar.getInstance()

    try {
        calendar.time = getDateFormat().parse(it)
    } catch (e: Throwable) {
        e.printStackTrace()
    }

    return calendar
}

fun Calendar.getString(): String = let { getDateFormat().format(it.time) }

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