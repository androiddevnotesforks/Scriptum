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
import android.widget.ProgressBar
import androidx.annotation.DimenRes
import androidx.cardview.widget.CardView
import sgtmelon.extensions.getDimen
import sgtmelon.test.idling.getWaitIdling

const val ALPHA_MIN = 0f
const val ALPHA_MAX = 1f

fun getAlphaAnimator(view: View, alphaTo: Float): Animator {
    return ObjectAnimator.ofFloat(view, View.ALPHA, view.alpha, alphaTo)
}

/** Provide fade animator and prepare [view] before animation run. */
fun getAlphaAnimator(view: View, visibleTo: Boolean) : Animator? {
    /** If it has the same [visibleTo] value -> it must not be animated. */
    if (view.isVisible() == visibleTo) return null

    val alphaFrom = if (visibleTo) ALPHA_MIN else ALPHA_MAX
    val alphaTo = if (visibleTo) ALPHA_MAX else ALPHA_MIN

    /** Prepare view before animation. */
    view.makeVisible()
    view.alpha = alphaFrom

    return getAlphaAnimator(view, alphaTo)
}

fun getScaleXAnimator(view: View, scaleTo: Float): Animator {
    return ObjectAnimator.ofFloat(view, View.SCALE_X, view.scaleX, scaleTo)
}

fun getScaleYAnimator(view: View, scaleTo: Float): Animator {
    return ObjectAnimator.ofFloat(view, View.SCALE_Y, view.scaleX, scaleTo)
}

fun getElevationAnimator(view: CardView, @DimenRes elevationTo: Int): Animator {
    return getElevationAnimator(view, view.context.getDimen(elevationTo).toFloat())
}

fun getElevationAnimator(view: CardView, elevationTo: Float): Animator {
    return view.getElevationAnimator(view.cardElevation, elevationTo)
}

private fun CardView.getElevationAnimator(valueFrom: Float, valueTo: Float): ValueAnimator {
    return ValueAnimator.ofFloat(valueFrom, valueTo).apply {
        addUpdateListener {
            cardElevation = it.animatedValue as? Float ?: return@addUpdateListener
        }
    }
}

fun getProgressAnimator(view: ProgressBar, max: Int, done: Int): Animator {
    view.max = max * PROGRESS_ANIM_SCALE
    return ObjectAnimator.ofInt(view, "progress", done * PROGRESS_ANIM_SCALE)
}

/**
 * Variable for increase [ProgressBar.setMax] and [ProgressBar.setProgress] values, and change of
 * them will look smoother.
 */
const val PROGRESS_ANIM_SCALE = 2500

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
            override fun onAnimationEnd(animation: Animator?) = onEnd()
        })
    }

    animator.start()
    getWaitIdling().start(duration)

    return animator
}