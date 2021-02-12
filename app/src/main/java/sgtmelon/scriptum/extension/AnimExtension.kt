package sgtmelon.scriptum.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.DimenRes
import androidx.cardview.widget.CardView
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import sgtmelon.scriptum.R
import sgtmelon.scriptum.idling.AppIdlingResource
import sgtmelon.scriptum.idling.IdlingTag

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

        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) = onEnd()
        })
    }.start()
}

fun Transition.addIdlingListener(): Transition = apply {
    addListener(object : TransitionListenerAdapter() {
        override fun onTransitionStart(transition: Transition) {
            AppIdlingResource.getInstance().startHardWork(IdlingTag.Anim.TRANSITION)
        }

        override fun onTransitionEnd(transition: Transition) {
            AppIdlingResource.getInstance().stopHardWork(IdlingTag.Anim.TRANSITION)
        }

        override fun onTransitionCancel(transition: Transition) {
            AppIdlingResource.getInstance().stopHardWork(IdlingTag.Anim.TRANSITION)
        }

        override fun onTransitionPause(transition: Transition) {
            AppIdlingResource.getInstance().stopHardWork(IdlingTag.Anim.TRANSITION)
        }

        override fun onTransitionResume(transition: Transition) {
            AppIdlingResource.getInstance().startHardWork(IdlingTag.Anim.TRANSITION)
        }
    })
}

fun Animator.addIdlingListener(): Animator = apply {
    addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            AppIdlingResource.getInstance().startHardWork(IdlingTag.Anim.TRANSITION)
        }

        override fun onAnimationEnd(animation: Animator?) {
            AppIdlingResource.getInstance().stopHardWork(IdlingTag.Anim.TRANSITION)
        }

        override fun onAnimationCancel(animation: Animator?) {
            AppIdlingResource.getInstance().stopHardWork(IdlingTag.Anim.TRANSITION)
        }

        override fun onAnimationPause(animation: Animator?) {
            AppIdlingResource.getInstance().stopHardWork(IdlingTag.Anim.TRANSITION)

        }

        override fun onAnimationResume(animation: Animator?) {
            AppIdlingResource.getInstance().startHardWork(IdlingTag.Anim.TRANSITION)
        }
    })
}