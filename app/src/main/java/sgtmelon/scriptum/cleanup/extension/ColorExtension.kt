package sgtmelon.scriptum.cleanup.extension

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.MenuItem
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import sgtmelon.extensions.getColorAttr
import sgtmelon.extensions.getColorCompat
import sgtmelon.extensions.getDimen
import sgtmelon.extensions.getDrawableCompat
import sgtmelon.extensions.setColorFilterCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.data.ColorData.accent
import sgtmelon.scriptum.cleanup.domain.model.data.ColorData.dark
import sgtmelon.scriptum.cleanup.domain.model.data.ColorData.light
import sgtmelon.scriptum.cleanup.domain.model.item.ColorItem
import sgtmelon.scriptum.cleanup.domain.model.key.ColorShade
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed

/**
 * Get note color for toolbars rely on theme and background
 * [needDark] - If element place on dark background (e.g. note color indicator)
 */
@ColorInt
fun Context.getNoteToolbarColor(theme: ThemeDisplayed, color: Color, needDark: Boolean): Int {
    val ordinal = color.ordinal

    return if (theme == ThemeDisplayed.LIGHT) {
        if (needDark) getColorCompat(dark[ordinal])
        else getColorCompat(light[ordinal])
    } else {
        if (needDark) getColorCompat(dark[ordinal])
        else getColorAttr(R.attr.clPrimary)
    }
}

/**
 * Get note color for cards
 */
@ColorInt
fun Context.getNoteCardColor(color: Color): Int = getColorCompat(light[color.ordinal])

@ColorInt
fun Context.getAppSimpleColor(color: Color, shade: ColorShade): Int {
    val ordinal = color.ordinal

    return getColorCompat(
        when (shade) {
            ColorShade.LIGHT -> light[ordinal]
            ColorShade.ACCENT -> accent[ordinal]
            ColorShade.DARK -> dark[ordinal]
        }
    )
}

fun MenuItem.tintIcon(context: Context) {
    val icon = this.icon ?: return

    val wrapDrawable = DrawableCompat.wrap(icon)
    DrawableCompat.setTint(wrapDrawable, context.getColorAttr(R.attr.clContent))
    this.icon = wrapDrawable
}

fun Context.getTintDrawable(@DrawableRes id: Int, @AttrRes tint: Int = R.attr.clContent): Drawable? {
    val drawable = getDrawableCompat(id) ?: return null

    drawable.setColorFilterCompat(getColorAttr(tint))

    return drawable
}

fun Drawable.setColor(context: Context, colorItem: ColorItem) {
    if (this !is GradientDrawable) return

    setColor(context.getColorCompat(colorItem.fill))
    setStroke(context.getDimen(value = 1f), context.getColorCompat(colorItem.stroke))
}