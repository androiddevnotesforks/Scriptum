package sgtmelon.scriptum.office.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.MenuItem;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
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
     * @param color  - Идентификатор цвета заметки
     * @param onDark - Если элемент находится на тёмном фоне (например индикатор цвета заметки
     * @return - Один из стандартных цветов приложения
     */
    public static int get(Context context, @ColorDef int color, boolean onDark) {
        switch (PrefUtils.getTheme(context)) {
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

    /**
     * Получение RGB промежуточного цвета в записимости от значения трансформации
     *
     * @param from  - Цвет, от которого происходит переход
     * @param to    - Цвет, к которому происходит переход
     * @param ratio - Текущее положение трансформации
     * @return - Промежуточный цвет
     */
    public static int blend(int from, int to, @FloatRange(from = 0.0, to = 1.0) float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }

    /**
     * Покараска элемента меню в стандартный цвет
     *
     * @param item - Элемент меню
     */
    public static void tintMenuIcon(Context context, MenuItem item) {
        final Drawable drawable = item.getIcon();
        final Drawable wrapDrawable = DrawableCompat.wrap(drawable);

        final int color = get(context, R.attr.clIcon);
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
    public static Drawable getDrawable(Context context, @DrawableRes int id, @AttrRes int attr) {
        final Drawable drawable = ContextCompat.getDrawable(context, id);

        final int color = get(context, attr);
        if (drawable != null) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }

        return drawable;
    }

}