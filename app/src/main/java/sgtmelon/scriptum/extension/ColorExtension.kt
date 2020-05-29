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
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.data.ColorData.accent
import sgtmelon.scriptum.domain.model.data.ColorData.dark
import sgtmelon.scriptum.domain.model.data.ColorData.light
import sgtmelon.scriptum.domain.model.item.ColorItem
import sgtmelon.scriptum.domain.model.key.ColorShade

/**
 * Get note color rely on theme and background
 * [needDark] - If element place on dark background (e.g. note color indicator)
 */
@ColorInt fun Context.getAppThemeColor(@Theme theme: Int, @Color color: Int, needDark: Boolean) =
        if (theme == Theme.LIGHT) {
            if (needDark) getCompatColor(dark[color])
            else getCompatColor(light[color])
        } else {
            if (needDark) getCompatColor(dark[color])
            else getColorAttr(R.attr.clPrimary)
        }

@ColorInt fun Context.getColorAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()

    theme.resolveAttribute(attr, typedValue, true)

    return ContextCompat.getColor(this, typedValue.resourceId)
}

fun Context.getCompatColor(@ColorRes id: Int) = let { ContextCompat.getColor(it, id) }

fun Context.getCompatDrawable(@DrawableRes id: Int) = let { ContextCompat.getDrawable(it, id) }

@ColorInt fun Context.getAppSimpleColor(@Color color: Int, shade: ColorShade): Int {
    return getCompatColor(when(shade) {
        ColorShade.LIGHT -> light[color]
        ColorShade.ACCENT -> accent[color]
        ColorShade.DARK -> dark[color]
    })
}

fun MenuItem.tintIcon(context: Context) {
    val wrapDrawable = DrawableCompat.wrap(icon)

    DrawableCompat.setTint(wrapDrawable, context.getColorAttr(R.attr.clContent))

    this.icon = wrapDrawable
}

fun Context.getTintDrawable(@DrawableRes id: Int, @AttrRes tint: Int = R.attr.clContent): Drawable? {
    val drawable = getCompatDrawable(id) ?: return null

    drawable.setColorFilter(getColorAttr(tint), PorterDuff.Mode.SRC_ATOP)

    return drawable
}

fun Drawable.setColor(context: Context, colorItem: ColorItem) {
    if (this !is GradientDrawable) return

    setColor(context.getCompatColor(colorItem.fill))
    setStroke(context.getDimen(value = 1f), context.getCompatColor(colorItem.stroke))
}