package sgtmelon.scriptum.extension

import android.animation.ValueAnimator
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun View.addSystemInsetsPadding(dir: InsetsDir, withRemove: Boolean, targetView: View? = this) {
    if (targetView == null) return

    doOnApplyWindowInsets { _, insets, padding, _ ->
        targetView.updatePadding(dir, insets, padding)

        return@doOnApplyWindowInsets if (withRemove) {
            insets.removeSystemWindowInsets(dir)
        } else {
            insets
        }
    }
}

/**
 * [this] - если определён другой [targetView], то это должен быть viewGroup
 * [targetView] - view для которой в первую очередь применяется inset
 * [withRemove] - определяет, будет ли затёрт нужный inset после пременения
 */
fun View.addSystemInsetsMargin(
    dir: InsetsDir,
    withRemove: Boolean,
    targetView: View? = this
) {
    if (targetView == null) return

    val initialMargin = recordInitialMargin(targetView) ?: return

    doOnApplyWindowInsets { _, insets, _, _ ->
        targetView.updateMargin(dir, insets, initialMargin)

        return@doOnApplyWindowInsets if (withRemove) {
            insets.removeSystemWindowInsets(dir)
        } else {
            insets
        }
    }
}

inline fun View.doOnApplyWindowInsets(
    crossinline block: (view: View, insets: WindowInsetsCompat, padding: Rect, margin: Rect?) -> WindowInsetsCompat
) {
    val initialPadding = recordInitialPadding(this)
    val initialMargin = recordInitialMargin(this)

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        block(view, insets, initialPadding, initialMargin)
    }

    requestApplyInsetsWhenAttached()
}

fun View.removeWindowInsetsListener() = ViewCompat.setOnApplyWindowInsetsListener(this, null)

fun recordInitialPadding(view: View): Rect {
    return Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)
}

fun recordInitialMargin(view: View): Rect? {
    val params = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return null

    return Rect(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin)
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                v?.addOnAttachStateChangeListener(this)
            }

            override fun onViewAttachedToWindow(v: View?) {
                v?.removeOnAttachStateChangeListener(this)
                v?.requestApplyInsets()
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

    return WindowInsetsCompat.Builder(this).setSystemWindowInsets(Insets.of(newRect)).build()
}

fun View.updatePadding(dir: InsetsDir, insets: WindowInsetsCompat, padding: Rect) = when (dir) {
    InsetsDir.LEFT -> updatePadding(left = padding.left + insets.systemWindowInsetLeft)
    InsetsDir.TOP -> updatePadding(top = padding.top + insets.systemWindowInsetTop)
    InsetsDir.RIGHT -> updatePadding(right = padding.right + insets.systemWindowInsetRight)
    InsetsDir.BOTTOM -> updatePadding(bottom = padding.bottom + insets.systemWindowInsetBottom)
}

fun View.updatePadding(
    left: Int = paddingLeft,
    top: Int = paddingTop,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) = setPadding(left, top, right, bottom)

fun View.updateMargin(
    dir: InsetsDir,
    insets: WindowInsetsCompat,
    margin: Rect?,
    withAnimation: Boolean = false
) {
    if (margin == null) return

    val valueTo = when(dir) {
        InsetsDir.LEFT -> margin.left + insets.systemWindowInsetLeft
        InsetsDir.TOP -> margin.top + insets.systemWindowInsetTop
        InsetsDir.RIGHT -> margin.right + insets.systemWindowInsetRight
        InsetsDir.BOTTOM -> margin.bottom + insets.systemWindowInsetBottom
    }

    if (withAnimation) {
        updateMarginAnimation(dir, valueTo, this)
    } else when (dir) {
        InsetsDir.LEFT -> updateMargin(left = valueTo)
        InsetsDir.TOP -> updateMargin(top = valueTo)
        InsetsDir.RIGHT -> updateMargin(right = valueTo)
        InsetsDir.BOTTOM -> updateMargin(bottom = valueTo)
    }
}

private fun updateMarginAnimation(
    dir: InsetsDir,
    valueTo: Int,
    view: View
) {
    val params = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return
    val valueFrom = when(dir) {
        InsetsDir.LEFT -> params.leftMargin
        InsetsDir.TOP -> params.topMargin
        InsetsDir.RIGHT -> params.rightMargin
        InsetsDir.BOTTOM -> params.bottomMargin
    }

    ValueAnimator.ofInt(valueFrom, valueTo).apply {
        this.interpolator = DecelerateInterpolator()
        this.duration = 35L

        addUpdateListener {
            val value = it.animatedValue as? Int ?: return@addUpdateListener

            when (dir) {
                InsetsDir.LEFT -> view.updateMargin(left = value)
                InsetsDir.TOP -> view.updateMargin(top = value)
                InsetsDir.RIGHT -> view.updateMargin(right = value)
                InsetsDir.BOTTOM -> view.updateMargin(bottom = value)
            }
        }
    }.start()
}

fun View.updateMargin(
    left: Int? = (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin,
    top: Int? = (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin,
    right: Int? = (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin,
    bottom: Int? = (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin
) {
    if (left == null || top == null || right == null || bottom == null) return

    val params = layoutParams as? ViewGroup.MarginLayoutParams ?: return
    params.setMargins(left, top, right, bottom)
    layoutParams = params
}

enum class InsetsDir { LEFT, TOP, RIGHT, BOTTOM }