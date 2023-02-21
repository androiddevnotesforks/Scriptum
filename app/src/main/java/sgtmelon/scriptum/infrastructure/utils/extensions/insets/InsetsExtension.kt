package sgtmelon.scriptum.infrastructure.utils.extensions.insets

import android.animation.ValueAnimator
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sgtmelon.scriptum.R
import sgtmelon.test.idling.getWaitIdling

fun View.addSystemInsetsPadding(
    dir: InsetsDir,
    targetView: View?,
    withRemove: Boolean = true
) {
    if (targetView == null) return

    val initialPadding = targetView.getInitialPadding()
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

    val initialMargin = targetView.getInitialMargin()
    doOnApplyWindowInsets { _, insets, _, _, _ ->
        targetView.updateMargin(dir, insets, initialMargin)

        return@doOnApplyWindowInsets if (withRemove) {
            insets.removeSystemWindowInsets(dir)
        } else {
            insets
        }
    }
}

inline fun View.doOnApplyWindowInsets(
    crossinline block: (
        view: View,
        insets: WindowInsetsCompat,
        isFirstTime: Boolean,
        padding: Rect,
        margin: Rect
    ) -> WindowInsetsCompat
) {
    val initialPadding = getInitialPadding()
    val initialMargin = this.getInitialMargin()

    /** Variable for detect first applying of insets. */
    var isFirstTime = true

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val result = block(view, insets, isFirstTime, initialPadding, initialMargin)

        if (isFirstTime) {
            isFirstTime = false
        }

        return@setOnApplyWindowInsetsListener result
    }

    requestApplyInsetsWhenAttached()
}

fun View.removeWindowInsetsListener() {
    ViewCompat.setOnApplyWindowInsetsListener(this, null)
}

fun View.getInitialPadding(): Rect {
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

fun View.getInitialMargin(): Rect {
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

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View) {
                v.addOnAttachStateChangeListener(this)
            }

            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }
        })
    }
}

fun WindowInsetsCompat.removeSystemWindowInsets(dir: InsetsDir): WindowInsetsCompat {
    val newRect = Rect(
        if (dir == InsetsDir.LEFT) 0 else systemWindowInsetLeft,
        if (dir == InsetsDir.TOP) 0 else systemWindowInsetTop,
        if (dir == InsetsDir.RIGHT) 0 else systemWindowInsetRight,
        if (dir == InsetsDir.BOTTOM) 0 else systemWindowInsetBottom
    )

    return WindowInsetsCompat.Builder(this)
        .setSystemWindowInsets(Insets.of(newRect))
        .build()
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
        updateWithAnimation(getMargin(dir), valueTo) { updateMargin(dir, it) }
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

private inline fun View.updateWithAnimation(
    valueFrom: Int,
    valueTo: Int,
    crossinline onChange: (Int) -> Unit
) {
    val duration = resources.getInteger(R.integer.keyboard_change_time).toLong()
    ValueAnimator.ofInt(valueFrom, valueTo).apply {
        this.interpolator = AccelerateDecelerateInterpolator()
        this.duration = duration
        addUpdateListener { onChange(it.animatedValue as Int) }
    }.start()

    getWaitIdling().start(duration)
}

enum class InsetsDir { LEFT, TOP, RIGHT, BOTTOM }

/**
 * Functions for setup simple Insets for [View], for remove repeatable code and decrease
 * possible errors count.
 */
fun View.setPaddingInsets(vararg insetsDirs: InsetsDir) {
    doOnApplyWindowInsets { view, insets, _, padding, _ ->
        for (dir in insetsDirs) {
            view.updatePadding(dir, insets, padding)
        }
        return@doOnApplyWindowInsets insets
    }
}

fun View.setMarginInsets(vararg insetsDirs: InsetsDir) {
    doOnApplyWindowInsets { view, insets, _, _, margin ->
        for (dir in insetsDirs) {
            view.updateMargin(dir, insets, margin)
        }
        return@doOnApplyWindowInsets insets
    }
}