package sgtmelon.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat


fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable? {
    return AppCompatResources.getDrawable(this, resId)
}

fun Drawable.setColorFilterCompat(
    @ColorInt color: Int,
    mode: BlendModeCompat = BlendModeCompat.SRC_ATOP
) {
    colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, mode)
}