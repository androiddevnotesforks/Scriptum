package sgtmelon.scriptum.infrastructure.utils.extensions.insets

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.extensions.updateWithAnimation

/**
 * [this] - must be viewGroup
 * [targetView] - view, for which need apply insets first.
 * [withRemove] - describes, will be cleared needed insets after apply or not.
 */
fun View.addSystemInsetsMargin(
    dir: InsetsDir,
    targetView: View?,
    withRemove: Boolean = true
) {
    if (targetView == null) return

    val initialMargin = targetView.getInitMargin()
    doOnApplyWindowInsets { _, insets, _, _, _ ->
        targetView.updateMargin(dir, insets, initialMargin)

        return@doOnApplyWindowInsets if (withRemove) {
            insets.removeSystemWindowInsets(dir)
        } else {
            insets
        }
    }
}

/**
 * Functions for setup simple Insets for [View], needed to remove repeatable code and decrease
 * possible errors.
 */
fun View.setMarginInsets(vararg insetsDirs: InsetsDir) {
    doOnApplyWindowInsets { view, insets, _, _, margin ->
        for (dir in insetsDirs) {
            view.updateMargin(dir, insets, margin)
        }
        return@doOnApplyWindowInsets insets
    }
}

fun View.getInitMargin(): Rect {
    return Rect(
        getMargin(InsetsDir.LEFT), getMargin(InsetsDir.TOP),
        getMargin(InsetsDir.RIGHT), getMargin(InsetsDir.BOTTOM)
    )
}

private fun View.getMargin(dir: InsetsDir): Int {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    return when (dir) {
        InsetsDir.LEFT -> params.leftMargin
        InsetsDir.TOP -> params.topMargin
        InsetsDir.RIGHT -> params.rightMargin
        InsetsDir.BOTTOM -> params.bottomMargin
    }
}

fun View.updateMargin(
    dir: InsetsDir,
    insets: WindowInsetsCompat,
    margin: Rect,
    withAnimation: Boolean = false
) {
    val valueTo = when (dir) {
        InsetsDir.LEFT -> margin.left + insets.systemWindowInsetLeft
        InsetsDir.TOP -> margin.top + insets.systemWindowInsetTop
        InsetsDir.RIGHT -> margin.right + insets.systemWindowInsetRight
        InsetsDir.BOTTOM -> margin.bottom + insets.systemWindowInsetBottom
    }

    if (withAnimation) {
        val duration = resources.getInteger(R.integer.keyboard_change_time).toLong()
        updateWithAnimation(duration, getMargin(dir), valueTo) { updateMargin(dir, it) }
    } else {
        updateMargin(dir, valueTo)
    }
}

private fun View.updateMargin(dir: InsetsDir, valueTo: Int) = when (dir) {
    InsetsDir.LEFT -> updateMargin(left = valueTo)
    InsetsDir.TOP -> updateMargin(top = valueTo)
    InsetsDir.RIGHT -> updateMargin(right = valueTo)
    InsetsDir.BOTTOM -> updateMargin(bottom = valueTo)
}

fun View.updateMargin(
    left: Int = (layoutParams as ViewGroup.MarginLayoutParams).leftMargin,
    top: Int = (layoutParams as ViewGroup.MarginLayoutParams).topMargin,
    right: Int = (layoutParams as ViewGroup.MarginLayoutParams).rightMargin,
    bottom: Int = (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin
) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(left, top, right, bottom)
    layoutParams = params
}
