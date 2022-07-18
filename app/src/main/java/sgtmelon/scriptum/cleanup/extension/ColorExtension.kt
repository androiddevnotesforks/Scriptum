package sgtmelon.scriptum.cleanup.extension

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.MenuItem
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.data.ColorData.accent
import sgtmelon.scriptum.cleanup.domain.model.data.ColorData.dark
import sgtmelon.scriptum.cleanup.domain.model.data.ColorData.light
import sgtmelon.scriptum.cleanup.domain.model.item.ColorItem
import sgtmelon.scriptum.cleanup.domain.model.key.ColorShade
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed

/**
 * Get note color for toolbars rely on theme and background
 * [needDark] - If element place on dark background (e.g. note color indicator)
 */
@ColorInt
fun Context.getNoteToolbarColor(theme: ThemeDisplayed, @Color color: Int, needDark: Boolean): Int {
    return if (theme == ThemeDisplayed.LIGHT) {
        if (needDark) getCompatColor(dark[color])
        else getCompatColor(light[color])
    } else {
        if (needDark) getCompatColor(dark[color])
        else getColorAttr(R.attr.clPrimary)
    }
}

/**
 * Get note color for cards
 */
@ColorInt
fun Context.getNoteCardColor(@Color color: Int): Int = getCompatColor(light[color])

@ColorInt
fun Context.getAppSimpleColor(@Color color: Int, shade: ColorShade): Int {
    return getCompatColor(when (shade) {
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
    val drawable = getDrawable(id) ?: return null

    drawable.setColorFilter(getColorAttr(tint), PorterDuff.Mode.SRC_ATOP)

    return drawable
}

fun Drawable.setColor(context: Context, colorItem: ColorItem) {
    if (this !is GradientDrawable) return

    setColor(context.getCompatColor(colorItem.fill))
    setStroke(context.getDimen(value = 1f), context.getCompatColor(colorItem.stroke))
}