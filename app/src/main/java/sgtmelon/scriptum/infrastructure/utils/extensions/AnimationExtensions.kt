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

inline fun updateWithAnimation(
    duration: Long,
    valueFrom: Int,
    valueTo: Int,
    crossinline onEnd: () -> Unit = {},
    crossinline onChange: (Int) -> Unit
) {
    ValueAnimator.ofInt(valueFrom, valueTo).apply {
        this.interpolator = AccelerateDecelerateInterpolator()
        this.duration = duration

        addUpdateListener { onChange(it.animatedValue as Int) }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) = onEnd()
        })
    }.start()

    getWaitIdling().start(duration)
}