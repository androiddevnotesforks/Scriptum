package sgtmelon.test.idling

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import sgtmelon.test.idling.impl.AppIdlingResource

private const val ANIM_TAG = "TRANSITION"

fun Transition.addIdlingListener(): Transition = apply {
    addListener(object : TransitionListenerAdapter() {
        override fun onTransitionStart(transition: Transition) {
            AppIdlingResource.getInstance().startWork(ANIM_TAG)
        }

        override fun onTransitionEnd(transition: Transition) {
            AppIdlingResource.getInstance().stopWork(ANIM_TAG)
        }

        override fun onTransitionCancel(transition: Transition) {
            AppIdlingResource.getInstance().stopWork(ANIM_TAG)
        }

        override fun onTransitionPause(transition: Transition) {
            AppIdlingResource.getInstance().stopWork(ANIM_TAG)
        }

        override fun onTransitionResume(transition: Transition) {
            AppIdlingResource.getInstance().startWork(ANIM_TAG)
        }
    })
}

fun Animator.addIdlingListener(): Animator = apply {
    addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            AppIdlingResource.getInstance().startWork(ANIM_TAG)
        }

        override fun onAnimationEnd(animation: Animator?) {
            AppIdlingResource.getInstance().stopWork(ANIM_TAG)
        }

        override fun onAnimationCancel(animation: Animator?) {
            AppIdlingResource.getInstance().stopWork(ANIM_TAG)
        }

        override fun onAnimationPause(animation: Animator?) {
            AppIdlingResource.getInstance().stopWork(ANIM_TAG)

        }

        override fun onAnimationResume(animation: Animator?) {
            AppIdlingResource.getInstance().startWork(ANIM_TAG)
        }
    })
}