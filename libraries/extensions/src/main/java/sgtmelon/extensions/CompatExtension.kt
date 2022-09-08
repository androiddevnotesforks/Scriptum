package sgtmelon.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat

fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable? {
    return AppCompatResources.getDrawable(this, resId)
}

fun Context.getColorCompat(@ColorRes id: Int) = let { ContextCompat.getColor(it, id) }

fun Drawable.setColorFilterCompat(
    @ColorInt color: Int,
    mode: BlendModeCompat = BlendModeCompat.SRC_ATOP
) {
    colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, mode)
}