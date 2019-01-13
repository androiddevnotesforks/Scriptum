package sgtmelon.scriptum.office.utils;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.ThemeDef;

/**
 * Класс содержащий адаптеры для dataBinding
 */
public final class BindUtils {

    // TODO: 10.12.2018 разобраться с requireAll =

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
                        ? sgtmelon.scriptum.office.utils.ColorUtils.get(context, trueColor)
                        : sgtmelon.scriptum.office.utils.ColorUtils.get(context, falseColor),
                PorterDuff.Mode.SRC_ATOP
        );
    }

    @BindingAdapter(value = {"boolExpression", "trueColor", "falseColor"})
    public static void setTextColor(TextView textView, boolean boolExpression,
                                    @AttrRes int trueColor, @AttrRes int falseColor) {
        final Context context = textView.getContext();

        textView.setTextColor(boolExpression
                ? sgtmelon.scriptum.office.utils.ColorUtils.get(context, trueColor)
                : sgtmelon.scriptum.office.utils.ColorUtils.get(context, falseColor)
        );
    }

    @BindingAdapter("noteColor")
    public static void setCardBackgroundColor(CardView cardView, @ColorDef int color) {
        final Context context = cardView.getContext();

        cardView.setCardBackgroundColor(
                sgtmelon.scriptum.office.utils.ColorUtils.get(context, color, false)
        );
    }

    @BindingAdapter(value = {"noteColor", "viewOnDark"})
    public static void setTint(ImageView imageView, @ColorDef int color,
                               boolean viewOnDark) {
        final Context context = imageView.getContext();

        imageView.setColorFilter(
                sgtmelon.scriptum.office.utils.ColorUtils.get(context, color, viewOnDark),
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
            imageView.setColorFilter(sgtmelon.scriptum.office.utils.ColorUtils.get(context, color), PorterDuff.Mode.SRC_ATOP);
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
                        ? sgtmelon.scriptum.office.utils.ColorUtils.get(context, trueColor)
                        : sgtmelon.scriptum.office.utils.ColorUtils.get(context, falseColor)
                        : sgtmelon.scriptum.office.utils.ColorUtils.get(context, falseColor),
                PorterDuff.Mode.SRC_ATOP
        );

        imageButton.setEnabled(boolExpression && extraExpression);
    }

    @BindingAdapter("enabled")
    public static void setEnabled(ImageButton imageButton, boolean enabled) {
        imageButton.setEnabled(enabled);
    }

    @BindingAdapter("visibleOn")
    public static void setVisibility(View view, @ThemeDef int visibleTheme) {
        final Context context = view.getContext();
        final int currentTheme = PrefUtils.getTheme(context);

        view.setVisibility(currentTheme == visibleTheme
                ? View.VISIBLE
                : View.GONE);
    }

    /**
     * @param toggle
     * @param state
     */
    @BindingAdapter(value = {"checkToggle", "checkState"})
    public static void setCheckBoxCheck(@NonNull CheckBox checkBox, boolean toggle,
                                        boolean state) {
        if (toggle) {
            checkBox.toggle();
        } else {
            checkBox.setChecked(state);
        }
    }

}