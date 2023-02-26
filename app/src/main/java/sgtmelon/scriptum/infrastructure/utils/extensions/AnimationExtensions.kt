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
import sgtmelon.extensions.PERCENT_MAX
import sgtmelon.extensions.PERCENT_MIN
import sgtmelon.extensions.getDimen
import sgtmelon.test.idling.getWaitIdling

const val ALPHA_MIN = 0f
const val ALPHA_MAX = 1f

fun getAlphaAnimator(view: View, alphaTo: Float): Animator {
    return ObjectAnimator.ofFloat(view, View.ALPHA, view.alpha, alphaTo)
}

fun getScaleXAnimator(view: View, scaleTo: Float): Animator {
    return ObjectAnimator.ofFloat(view, View.SCALE_X, view.scaleX, scaleTo)
}

fun getScaleYAnimator(view: View, scaleTo: Float): Animator {
    return ObjectAnimator.ofFloat(view, View.SCALE_Y, view.scaleX, scaleTo)
}

fun getElevationAnimator(view: CardView, @DimenRes elevationTo: Int): Animator {
    val valueFrom = view.cardElevation
    val valueTo = view.context.getDimen(elevationTo).toFloat()
    return view.getElevationAnimator(valueFrom, valueTo)
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

fun getAlphaInterpolator(isVisible: Boolean): Interpolator {
    return if (isVisible) AccelerateInterpolator() else DecelerateInterpolator()
}

inline fun animatePercent(
    duration: Long,
    interpolator: Interpolator = AccelerateDecelerateInterpolator(),
    crossinline onEnd: () -> Unit = {},
    crossinline onChange: (Int) -> Unit
): Animator = animateValue(PERCENT_MIN, PERCENT_MAX, duration, interpolator, onEnd, onChange)

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