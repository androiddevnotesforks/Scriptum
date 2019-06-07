package sgtmelon.scriptum.extension

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.MenuItem
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.ColorData.accent
import sgtmelon.scriptum.model.data.ColorData.dark
import sgtmelon.scriptum.model.data.ColorData.light
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import android.graphics.Color as ColorUtil

/**
 * Получение цвета заметки в зависимости от темы и заднего фона
 * [needDark] - Если элемент находится на тёмном фоне (например индикатор цвета заметки
 */
@ColorInt
fun Context.getAppThemeColor(@Color color: Int, needDark: Boolean) =
        if (PreferenceRepo(this).getTheme() == Theme.light) {
            if (needDark) getCompatColor(dark[color])
            else getCompatColor(light[color])
        } else {
            if (needDark) getCompatColor(dark[color])
            else getColorAttr(R.attr.clPrimary)
        }

/**
 * Получение цвета по аттрибуту, [attr] - аттрибут цвета
 */
@ColorInt fun Context.getColorAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()

    theme.resolveAttribute(attr, typedValue, true)

    return ContextCompat.getColor(this, typedValue.resourceId)
}

fun Context.getCompatColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)

@ColorInt fun Context.getAppSimpleColor(@Color color: Int, shade: ColorShade) =
        getCompatColor(when(shade) {
            ColorShade.LIGHT -> light[color]
            ColorShade.ACCENT -> accent[color]
            ColorShade.DARK -> dark[color]
        })

/**
 * Покараска элемента меню в стандартный цвет
 */
fun MenuItem.tintIcon(context: Context) {
    val drawable = this.icon
    val wrapDrawable = DrawableCompat.wrap(drawable)

    DrawableCompat.setTint(wrapDrawable, context.getColorAttr(R.attr.clContent))

    this.icon = wrapDrawable
}

/**
 * Получение покрашенного изображения
 */
fun Context.getTintDrawable(@DrawableRes id: Int): Drawable? {
    val drawable = ContextCompat.getDrawable(this, id) ?: return null
    drawable.setColorFilter(getColorAttr(R.attr.clContent), PorterDuff.Mode.SRC_ATOP)

    return drawable
}

/**
 * Получение RGB промежуточного цвета в записимости от значения трансформации
 * [ratio] - положение трансформации
 * [this] - цвет от которого идёт трансформация
 */
@ColorInt fun Int.blend(colorTo: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float): Int {
    val inverseRatio = 1f - ratio

    val r = ColorUtil.red(colorTo) * ratio + ColorUtil.red(this) * inverseRatio
    val g = ColorUtil.green(colorTo) * ratio + ColorUtil.green(this) * inverseRatio
    val b = ColorUtil.blue(colorTo) * ratio + ColorUtil.blue(this) * inverseRatio

    return ColorUtil.rgb(r.toInt(), g.toInt(), b.toInt())
}