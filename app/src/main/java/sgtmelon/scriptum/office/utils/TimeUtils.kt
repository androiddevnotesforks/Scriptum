package sgtmelon.scriptum.office.utils

import android.content.Context
import android.text.format.DateUtils
import sgtmelon.scriptum.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Класс для работы со временем, а так же @Singleton для ***
 */
object TimeUtils {

    private fun Context.getDateFormat(): SimpleDateFormat =
            SimpleDateFormat(getString(R.string.date_app_format), Locale.getDefault())

    private fun Locale.useAmPm(): Boolean =
            (DateFormat.getTimeInstance(DateFormat.FULL, this)).let {
                return it is SimpleDateFormat && it.toPattern().contains("a")
            }

    /**
     * Текущее время в нужном формате
     */
    fun Context.getTime(): String = getDateFormat().format(Calendar.getInstance().time)

    /**
     * Форматирует прошедшее время [date] в приятный вид
     */
    fun formatPast(context: Context, date: String): String? = try {
        val calendar = Calendar.getInstance().apply { time = context.getDateFormat().parse(date) }
        val locale = Locale.getDefault()

        SimpleDateFormat(when {
            calendar.isToday() -> if (locale.useAmPm()) context.getString(R.string.format_time_am) else context.getString(R.string.format_time)
            calendar.isThisYear() -> context.getString(R.string.format_date_medium)
            else -> context.getString(R.string.format_date_short)
        }, locale).format(calendar.time)
    } catch (e: Throwable) {
        null
    }

    private fun Calendar.isToday() = DateUtils.isToday(timeInMillis)

    private fun Calendar.isThisYear() = get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)

    fun formatFuture(context: Context, date: String): String? = try {
        val calendar = Calendar.getInstance().apply { time = context.getDateFormat().parse(date) }

        DateUtils.getRelativeDateTimeString(context, calendar.timeInMillis,
                DateUtils.DAY_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0
        ).toString()
    } catch (e: Throwable) {
        null
    }

}