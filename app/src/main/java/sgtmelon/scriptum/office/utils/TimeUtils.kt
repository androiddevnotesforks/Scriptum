package sgtmelon.scriptum.office.utils

import android.content.Context
import android.text.format.DateUtils
import sgtmelon.scriptum.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Класс для работы со временем, а так же @Singleton для ***
 */
object TimeUtils {

    private fun getDateFormat(context: Context): SimpleDateFormat {
        return SimpleDateFormat(context.getString(R.string.date_app_format), Locale.getDefault())
    }


    /**
     * Текущее время в нужном формате
     */
    fun Context.getTime(): String = getDateFormat(this).format(Calendar.getInstance().time)

    /**
     * @param date - Время создания/изменения заметки
     * @return - Время и дата в приятном виде
     */
    fun format(context: Context, date: String): String? {
        val formatOld = getDateFormat(context)

        try {
            val calendar = Calendar.getInstance()
            calendar.time = formatOld.parse(date)

            val formatNew = SimpleDateFormat(
                    if (DateUtils.isToday(calendar.timeInMillis))
                        context.getString(R.string.date_note_today_format)
                    else
                        context.getString(R.string.date_note_yesterday_format),
                    Locale.getDefault()
            )

            return formatNew.format(formatOld.parse(date))
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }

    }

}