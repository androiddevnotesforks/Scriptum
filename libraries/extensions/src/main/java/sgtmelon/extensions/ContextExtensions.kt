package sgtmelon.extensions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes


@ColorInt
fun Context.getColorAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()

    theme.resolveAttribute(attr, typedValue, true)

    return getColorCompat(typedValue.resourceId)
}

@DimenRes
fun Context.getDimenAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()

    theme.resolveAttribute(attr, typedValue, true)

    return typedValue.resourceId
}

fun Context.getDimen(value: Float): Int {
    val unit = TypedValue.COMPLEX_UNIT_DIP
    val metrics = resources.displayMetrics

    return TypedValue.applyDimension(unit, value, metrics).toInt()
}

fun Context.getDimen(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)