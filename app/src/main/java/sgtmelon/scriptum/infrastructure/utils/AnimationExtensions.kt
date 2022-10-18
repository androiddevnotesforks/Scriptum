package sgtmelon.scriptum.infrastructure.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.DimenRes
import androidx.cardview.widget.CardView


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
    val valueTo = view.context.resources.getDimensionPixelSize(elevationTo).toFloat()

    return ValueAnimator.ofFloat(valueFrom, valueTo).apply {
        addUpdateListener {
            view.cardElevation = it.animatedValue as? Float ?: return@addUpdateListener
        }
    }
}

fun getAlphaInterpolator(isVisible: Boolean): Interpolator {
    return if (isVisible) AccelerateInterpolator() else DecelerateInterpolator()
}