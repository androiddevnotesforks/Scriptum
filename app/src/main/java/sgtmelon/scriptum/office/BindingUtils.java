package sgtmelon.scriptum.office;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.annot.ColorAnn;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.ThemeDef;

/**
 * Класс содержащий адаптеры для dataBinding
 */
public final class BindingUtils {

    /**
     * Установка цветового фильтра на основании результата логического выражения
     *
     * @param boolExpression - Логическое выражение
     * @param trueColor      - Цвет при истине
     * @param falseColor     - Цвет если ложь
     */
    @BindingAdapter(value = {"boolExpression", "trueColor", "falseColor"})
    public static void setTint(ImageButton imageButton, boolean boolExpression,
                               @AttrRes int trueColor, @AttrRes int falseColor) {
        final Context context = imageButton.getContext();

        imageButton.setColorFilter(boolExpression
                        ? ColorUtils.get(context, trueColor)
                        : ColorUtils.get(context, falseColor),
                PorterDuff.Mode.SRC_ATOP
        );
    }

    @BindingAdapter(value = {"boolExpression", "trueColor", "falseColor"})
    public static void setTextColor(TextView textView, boolean boolExpression,
                                    @AttrRes int trueColor, @AttrRes int falseColor) {
        final Context context = textView.getContext();

        textView.setTextColor(boolExpression
                ? ColorUtils.get(context, trueColor)
                : ColorUtils.get(context, falseColor)
        );
    }

    @BindingAdapter("noteColor")
    public static void setCardBackgroundColor(CardView cardView, @ColorDef int color) {
        final Context context = cardView.getContext();

        cardView.setCardBackgroundColor(
                ColorUtils.get(context, color, false)
        );
    }

    @BindingAdapter(value = {"noteColor", "viewOnDark"})
    public static void setTint(ImageView imageView, @ColorDef int color,
                               boolean viewOnDark) {
        final Context context = imageView.getContext();

        imageView.setColorFilter(
                ColorUtils.get(context, color, viewOnDark),
                PorterDuff.Mode.SRC_ATOP
        );
    }

    @BindingAdapter(value = {"imageId", "imageColor"})
    public static void setImage(ImageView imageView, @DrawableRes int drawableId,
                                @AttrRes int color) {
        final Context context = imageView.getContext();
        final Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (drawable != null) {
            imageView.setImageDrawable(drawable);
            imageView.setColorFilter(ColorUtils.get(context, color), PorterDuff.Mode.SRC_ATOP);
        }
    }

    @BindingAdapter("time")
    public static void setNoteTime(TextView textView, String time) {
        textView.setText(TimeUtils.format(textView.getContext(), time));
    }

    /**
     * Блокирование кнопки и установка на неё цветового фильтра в зависимости
     * от логического выражения и дополнительного выражения
     *
     * @param boolExpression  - Логическое выржение, от которого зависит иконка и блокировка
     * @param extraExpression - Дополнительный параметр для контроля, например список содержит
     *                        текстовое сообщение
     */
    @BindingAdapter(value = {"boolExpression", "extraExpression", "trueColor", "falseColor"})
    public static void setTint(ImageButton imageButton, boolean boolExpression,
                               boolean extraExpression,
                               @AttrRes int trueColor, @AttrRes int falseColor) {
        final Context context = imageButton.getContext();

        imageButton.setColorFilter(boolExpression
                        ? extraExpression
                        ? ColorUtils.get(context, trueColor)
                        : ColorUtils.get(context, falseColor)
                        : ColorUtils.get(context, falseColor),
                PorterDuff.Mode.SRC_ATOP
        );

        imageButton.setEnabled(boolExpression && extraExpression);
    }

    @BindingAdapter("enabled")
    public static void setEnable(ImageButton imageButton, boolean enabled) {
        imageButton.setEnabled(enabled);
    }

    public static final class ColorUtils {

        /**
         * Получение цвета заметки в зависимости от темы и заднего фона
         *
         * @param color  - Идентификатор цвета заметки
         * @param onDark - Если элемент находится на тёмном фоне (например индикатор цвета заметки
         * @return - Один из стандартных цветов приложения
         */
        public static int get(Context context, @ColorDef int color, boolean onDark) {
            switch (HelpUtils.Pref.getTheme(context)) {
                case ThemeDef.light:
                    return ContextCompat.getColor(context, ColorAnn.cl_light[color]);
                case ThemeDef.dark:
                default:
                    return onDark
                            ? ContextCompat.getColor(context, ColorAnn.cl_dark[color])
                            : get(context, R.attr.clPrimary);
            }
        }

        /**
         * Получение цвета по аттрибуту
         *
         * @param attr - Аттрибут цвета
         * @return - Цвет в записимости от отрибута
         */
        public static int get(Context context, @AttrRes int attr) {
            final TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(attr, typedValue, true);
            return ContextCompat.getColor(context, typedValue.resourceId);
        }

    }

    public static final class TimeUtils {

        /**
         * @param date - Время создания/изменения заметки
         * @return - Время и дата в приятном виде
         */
        public static String format(Context context, String date) {
            final DateFormat formatOld = new SimpleDateFormat(context.getString(R.string.date_app_format), Locale.getDefault());

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

}