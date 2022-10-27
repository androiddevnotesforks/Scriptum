package sgtmelon.scriptum.infrastructure.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.DimenRes
import androidx.cardview.widget.CardView
import androidx.transition.Fade
import androidx.transition.Transition
import sgtmelon.test.idling.addIdlingListener


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

/**
 * Transition for animate hide and show of elements related with list (e.g. progressBar,
 * emptyInfo, recyclerView).
 */
fun getListTransition(duration: Int, vararg targets: View): Transition {
    val transition = Fade()
        .setDuration(duration.toLong())
        .setInterpolator(AccelerateDecelerateInterpolator())
        .addIdlingListener()

    for (view in targets) {
        transition.addTarget(view)
    }

    return transition
}