package sgtmelon.common.test

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import sgtmelon.common.test.idling.impl.AppIdlingResource

fun Transition.addIdlingListener(): Transition = apply {
    val tag = "TRANSITION"

    addListener(object : TransitionListenerAdapter() {
        override fun onTransitionStart(transition: Transition) {
            AppIdlingResource.getInstance().startWork(tag)
        }

        override fun onTransitionEnd(transition: Transition) {
            AppIdlingResource.getInstance().stopWork(tag)
        }

        override fun onTransitionCancel(transition: Transition) {
            AppIdlingResource.getInstance().stopWork(tag)
        }

        override fun onTransitionPause(transition: Transition) {
            AppIdlingResource.getInstance().stopWork(tag)
        }

        override fun onTransitionResume(transition: Transition) {
            AppIdlingResource.getInstance().startWork(tag)
        }
    })
}

fun Animator.addIdlingListener(): Animator = apply {
    val tag = "ANIMATION"

    addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            AppIdlingResource.getInstance().startWork(tag)
        }

        override fun onAnimationEnd(animation: Animator?) {
            AppIdlingResource.getInstance().stopWork(tag)
        }

        override fun onAnimationCancel(animation: Animator?) {
            AppIdlingResource.getInstance().stopWork(tag)
        }

        override fun onAnimationPause(animation: Animator?) {
            AppIdlingResource.getInstance().stopWork(tag)

        }

        override fun onAnimationResume(animation: Animator?) {
            AppIdlingResource.getInstance().startWork(tag)
        }
    })
}