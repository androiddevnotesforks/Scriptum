package sgtmelon.scriptum.infrastructure.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.MenuItem
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import sgtmelon.extensions.getColorAttr
import sgtmelon.extensions.getColorCompat
import sgtmelon.extensions.getDimen
import sgtmelon.extensions.getDrawableCompat
import sgtmelon.extensions.setColorFilterCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.item.ColorItem

fun MenuItem.tintIcon(context: Context) {
    val icon = this.icon ?: return

    val wrapDrawable = DrawableCompat.wrap(icon)
    DrawableCompat.setTint(wrapDrawable, context.getColorAttr(R.attr.clContent))
    this.icon = wrapDrawable
}

fun Context.getTintDrawable(
    @DrawableRes id: Int,
    @AttrRes tint: Int = R.attr.clContent
): Drawable? {
    val drawable = getDrawableCompat(id) ?: return null

    drawable.setColorFilterCompat(getColorAttr(tint))

    return drawable
}

/**
 * Use this tinting only indicator drawables.
 */
fun Drawable.setColor(context: Context, colorItem: ColorItem) {
    if (this !is GradientDrawable) return

    setColor(context.getColorCompat(colorItem.fill))
    setStroke(context.getDimen(value = 1f), context.getColorCompat(colorItem.stroke))
}