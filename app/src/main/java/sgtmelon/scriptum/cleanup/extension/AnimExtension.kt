package sgtmelon.scriptum.cleanup.extension

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.DimenRes
import androidx.cardview.widget.CardView
import androidx.core.animation.addListener
import sgtmelon.scriptum.R
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


fun getAlphaInterpolator(isVisible: Boolean) : Interpolator {
    return if (isVisible) AccelerateInterpolator() else DecelerateInterpolator()
}

inline fun View.animateAlpha(
    isVisible: Boolean,
    duration: Long = context.resources.getInteger(R.integer.info_fade_time).toLong(),
    crossinline onEnd: () -> Unit = {}
) {
    val interpolator = getAlphaInterpolator(isVisible)
    val valueTo = if (isVisible) 1f else 0f

    ObjectAnimator.ofFloat(this, View.ALPHA, valueTo).apply {
        this.interpolator = interpolator
        this.duration = duration

        addIdlingListener()
        addListener(onEnd = { onEnd() })
    }.start()
}