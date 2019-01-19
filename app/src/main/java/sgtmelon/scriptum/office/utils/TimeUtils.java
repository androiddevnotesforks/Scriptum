package sgtmelon.scriptum.office.utils;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import sgtmelon.scriptum.R;

/**
 * Класс для работы со временем, а так же @Singleton для ***
 */
public final class TimeUtils {

    @NonNull
    private static SimpleDateFormat getDateFormat(@NonNull Context context) {
        return new SimpleDateFormat(context.getString(R.string.date_app_format), Locale.getDefault());
    }

    /**
     * @return - Текущее время в нужном формате
     */
    public static String getTime(@NonNull Context context) {
        return getDateFormat(context).format(Calendar.getInstance().getTime());
    }

    /**
     * @param date - Время создания/изменения заметки
     * @return - Время и дата в приятном виде
     */
    static String format(@NonNull Context context, @NonNull String date) {
        final DateFormat formatOld = getDateFormat(context);

        try {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(formatOld.parse(date));

            final DateFormat formatNew = new SimpleDateFormat(
                    DateUtils.isToday(calendar.getTimeInMillis())
                            ? context.getString(R.string.date_note_today_format)
                            : context.getString(R.string.date_note_yesterday_format),
                    Locale.getDefault()
            );

            return formatNew.format(formatOld.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}