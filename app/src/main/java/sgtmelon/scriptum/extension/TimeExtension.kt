package sgtmelon.scriptum.extension

import android.content.Context
import android.text.format.DateUtils
import sgtmelon.scriptum.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun Context.getDateFormat(): SimpleDateFormat =
        SimpleDateFormat(getString(R.string.date_app_format), Locale.getDefault())

fun Locale.useAmPm(): Boolean =
        (DateFormat.getTimeInstance(DateFormat.FULL, this)).let {
            return it is SimpleDateFormat && it.toPattern().contains("a")
        }

/**
 * Текущее время в нужном формате
 */
fun Context.getTime(): String = getDateFormat().format(Calendar.getInstance().time)

fun Calendar.isToday() = DateUtils.isToday(timeInMillis)

fun Calendar.isThisYear() = get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)