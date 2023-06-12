package sgtmelon.scriptum.infrastructure.utils.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.DimenRes
import androidx.cardview.widget.CardView
import sgtmelon.extensions.getDimen
import sgtmelon.test.idling.getWaitIdling

const val ALPHA_MIN = 0f
const val ALPHA_MAX = 1f

fun View.getAlphaAnimator(alphaTo: Float): Animator {
    return ObjectAnimator.ofFloat(this, View.ALPHA, alpha, alphaTo)
}

/** Provide fade animator and prepare [this] before animation run. */
fun View.getAlphaAnimator(visibleTo: Boolean) : Animator? {
    /** If it has the same [visibleTo] value -> it must not be animated. */
    if (isVisible() == visibleTo) return null

    val alphaFrom = if (visibleTo) ALPHA_MIN else ALPHA_MAX
    val alphaTo = if (visibleTo) ALPHA_MAX else ALPHA_MIN

    /** Prepare view before animation. */
    makeVisible()
    alpha = alphaFrom

    return getAlphaAnimator(alphaTo)
}

fun View.getScaleXAnimator(scaleTo: Float): Animator {
    return ObjectAnimator.ofFloat(this, View.SCALE_X, scaleX, scaleTo)
}

fun View.getScaleYAnimator(scaleTo: Float): Animator {
    return ObjectAnimator.ofFloat(this, View.SCALE_Y, scaleX, scaleTo)
}

fun CardView.getElevationAnimator(@DimenRes elevationTo: Int): Animator {
    return this.getElevationAnimator(context.getDimen(elevationTo).toFloat())
}

fun CardView.getElevationAnimator(elevationTo: Float): Animator {
    return getElevationAnimator(cardElevation, elevationTo)
}

private fun CardView.getElevationAnimator(valueFrom: Float, valueTo: Float): ValueAnimator {
    return ValueAnimator.ofFloat(valueFrom, valueTo).apply {
        addUpdateListener {
            cardElevation = it.animatedValue as? Float ?: return@addUpdateListener
        }
    }
}

fun getAlphaInterpolator(isVisible: Boolean): Interpolator {
    return if (isVisible) AccelerateInterpolator() else DecelerateInterpolator()
}

inline fun animateValue(
    from: Int,
    to: Int,
    duration: Long,
    interpolator: Interpolator = AccelerateDecelerateInterpolator(),
    crossinline onEnd: () -> Unit = {},
    crossinline onChange: (Int) -> Unit
): Animator {
    val animator = ValueAnimator.ofInt(from, to).apply {
        this.duration = duration
        this.interpolator = interpolator

        addUpdateListener { onChange(it.animatedValue as Int) }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) = onEnd()
        })
    }

    animator.start()
    getWaitIdling().start(duration)

    return animator
}