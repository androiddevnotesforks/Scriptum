package sgtmelon.scriptum.office.utils

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.MenuItem
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.data.ColorData.dark
import sgtmelon.scriptum.model.data.ColorData.light
import sgtmelon.scriptum.office.annot.def.ColorDef
import sgtmelon.scriptum.office.annot.def.ThemeDef
import sgtmelon.scriptum.repository.Preference

/**
 * Получение цвета заметки в зависимости от темы и заднего фона
 * [needDark] - Если элемент находится на тёмном фоне (например индикатор цвета заметки
 */
@ColorInt fun Context.getAppThemeColor(@ColorDef color: Int, needDark: Boolean) =
        if (Preference(this).theme == ThemeDef.light) {
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

@ColorInt fun Context.getAppSimpleColor(@ColorDef color: Int, isLight: Boolean) =
        getCompatColor(if (isLight) light[color] else dark[color])

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

    val r = Color.red(colorTo) * ratio + Color.red(this) * inverseRatio
    val g = Color.green(colorTo) * ratio + Color.green(this) * inverseRatio
    val b = Color.blue(colorTo) * ratio + Color.blue(this) * inverseRatio

    return Color.rgb(r.toInt(), g.toInt(), b.toInt())
}