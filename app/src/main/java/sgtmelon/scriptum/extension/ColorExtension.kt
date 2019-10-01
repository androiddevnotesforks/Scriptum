package sgtmelon.scriptum.extension

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.MenuItem
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.ColorData.accent
import sgtmelon.scriptum.model.data.ColorData.dark
import sgtmelon.scriptum.model.data.ColorData.light
import sgtmelon.scriptum.model.item.ColorItem
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.repository.preference.PreferenceRepo

/**
 * Получение цвета заметки в зависимости от темы и заднего фона
 * [needDark] - Если элемент находится на тёмном фоне (например индикатор цвета заметки
 */
@ColorInt fun Context.getAppThemeColor(@Color color: Int, needDark: Boolean) =
        if (PreferenceRepo(context = this).theme == Theme.LIGHT) {
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

fun Drawable.setColor(context: Context, colorItem: ColorItem) {
    if (this !is GradientDrawable) return

    setColor(context.getCompatColor(colorItem.fill))
    setStroke(context.getDimen(value = 1f), context.getCompatColor(colorItem.stroke))
}