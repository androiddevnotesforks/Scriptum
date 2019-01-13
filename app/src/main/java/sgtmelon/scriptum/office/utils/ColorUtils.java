package sgtmelon.scriptum.office.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.MenuItem;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.annot.ColorAnn;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.ThemeDef;

public final class ColorUtils {

    /**
     * Получение цвета заметки в зависимости от темы и заднего фона
     *
     * @param color    - Идентификатор цвета заметки
     * @param needDark - Если элемент находится на тёмном фоне (например индикатор цвета заметки
     * @return - Один из стандартных цветов приложения
     */
    @ColorInt
    public static int get(@NonNull Context context, @ColorDef int color, boolean needDark) {
        switch (PrefUtils.getTheme(context)) {
            case ThemeDef.light:
                return needDark
                        ? ContextCompat.getColor(context, ColorAnn.cl_dark[color])
                        : ContextCompat.getColor(context, ColorAnn.cl_light[color]);
            case ThemeDef.dark:
            default:
                return needDark
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
    @ColorInt
    public static int get(@NonNull Context context, @AttrRes int attr) {
        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);

        return ContextCompat.getColor(context, typedValue.resourceId);
    }

    /**
     * Получение RGB промежуточного цвета в записимости от значения трансформации
     *
     * @param colorFrom - Цвет, от которого происходит переход
     * @param colorTo   - Цвет, к которому происходит переход
     * @param ratio     - Текущее положение трансформации
     * @return - Промежуточный цвет
     */
    @ColorInt
    public static int blend(@ColorInt int colorFrom, @ColorInt int colorTo,
                            @FloatRange(from = 0.0, to = 1.0) float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(colorTo) * ratio + Color.red(colorFrom) * inverseRatio;
        final float g = Color.green(colorTo) * ratio + Color.green(colorFrom) * inverseRatio;
        final float b = Color.blue(colorTo) * ratio + Color.blue(colorFrom) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }

    /**
     * Покараска элемента меню в стандартный цвет
     *
     * @param item - Элемент меню
     */
    public static void tintMenuIcon(@NonNull Context context, @NonNull MenuItem item) {
        final Drawable drawable = item.getIcon();
        final Drawable wrapDrawable = DrawableCompat.wrap(drawable);

        final int color = get(context, R.attr.clContent);
        DrawableCompat.setTint(wrapDrawable, color);

        item.setIcon(wrapDrawable);
    }

    /**
     * Получение покрашенного изображения
     *
     * @param id   - Идентификатор изображения
     * @param attr - Аттрибут цвета
     * @return - Покрашенное изображение
     */
    @Nullable
    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int id,
                                       @AttrRes int attr) {
        final Drawable drawable = ContextCompat.getDrawable(context, id);

        if (drawable == null) return null;

        final int color = get(context, attr);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        return drawable;
    }

}