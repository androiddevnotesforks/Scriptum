package sgtmelon.scriptum.infrastructure.utils.extensions.insets

import android.graphics.Rect
import android.view.View
import androidx.core.view.WindowInsetsCompat
import sgtmelon.scriptum.infrastructure.utils.extensions.updateWithAnimation

/**
 * [this] - must be viewGroup
 * [targetView] - view, for which need apply insets first.
 * [withRemove] - describes, will be cleared needed insets after apply or not.
 */
fun View.addSystemInsetsPadding(
    dir: InsetsDir,
    targetView: View?,
    withRemove: Boolean = true
) {
    if (targetView == null) return

    val initialPadding = targetView.getInitPadding()
    doOnApplyWindowInsets { _, insets, _, _, _ ->
        targetView.updatePadding(dir, insets, initialPadding)

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
fun View.setPaddingInsets(vararg dirs: InsetsDir) {
    doOnApplyWindowInsets { view, insets, _, padding, _ ->
        for (dir in dirs) {
            view.updatePadding(dir, insets, padding)
        }
        return@doOnApplyWindowInsets insets
    }
}

fun View.getInitPadding(): Rect {
    return Rect(
        getPadding(InsetsDir.LEFT), getPadding(InsetsDir.TOP),
        getPadding(InsetsDir.RIGHT), getPadding(InsetsDir.BOTTOM)
    )
}

private fun View.getPadding(dir: InsetsDir): Int {
    return when (dir) {
        InsetsDir.LEFT -> paddingLeft
        InsetsDir.TOP -> paddingTop
        InsetsDir.RIGHT -> paddingRight
        InsetsDir.BOTTOM -> paddingBottom
    }
}

fun View.updatePadding(
    dir: InsetsDir,
    insets: WindowInsetsCompat,
    padding: Rect,
    withAnimation: Boolean = false
) {
    val valueTo = when (dir) {
        InsetsDir.LEFT -> padding.left + insets.systemWindowInsetLeft
        InsetsDir.TOP -> padding.top + insets.systemWindowInsetTop
        InsetsDir.RIGHT -> padding.right + insets.systemWindowInsetRight
        InsetsDir.BOTTOM -> padding.bottom + insets.systemWindowInsetBottom
    }

    if (withAnimation) {
        updateWithAnimation(getPadding(dir), valueTo) { updatePadding(dir, it) }
    } else {
        updatePadding(dir, valueTo)
    }
}

private fun View.updatePadding(dir: InsetsDir, valueTo: Int) = when (dir) {
    InsetsDir.LEFT -> updatePadding(left = valueTo)
    InsetsDir.TOP -> updatePadding(top = valueTo)
    InsetsDir.RIGHT -> updatePadding(right = valueTo)
    InsetsDir.BOTTOM -> updatePadding(bottom = valueTo)
}

private fun View.updatePadding(
    left: Int = paddingLeft,
    top: Int = paddingTop,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) = setPadding(left, top, right, bottom)