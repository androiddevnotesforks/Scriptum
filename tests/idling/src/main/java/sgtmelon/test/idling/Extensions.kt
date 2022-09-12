package sgtmelon.test.idling

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import sgtmelon.test.idling.callback.AppIdlingCallback
import sgtmelon.test.idling.callback.WaitIdlingCallback
import sgtmelon.test.idling.impl.AppIdlingResource
import sgtmelon.test.idling.impl.WaitIdlingResource

fun getIdling(): AppIdlingCallback = AppIdlingResource.getInstance()

fun getWaitIdling(): WaitIdlingCallback = WaitIdlingResource.getInstance()

//region Animation

private const val ANIM_TAG = "TRANSITION"

fun Transition.addIdlingListener(): Transition = apply {
    addListener(object : TransitionListenerAdapter() {
        override fun onTransitionStart(transition: Transition) = getIdling().start(ANIM_TAG)
        override fun onTransitionEnd(transition: Transition) = getIdling().stop(ANIM_TAG)
        override fun onTransitionCancel(transition: Transition) = getIdling().stop(ANIM_TAG)
        override fun onTransitionPause(transition: Transition) = getIdling().stop(ANIM_TAG)
        override fun onTransitionResume(transition: Transition) = getIdling().start(ANIM_TAG)
    })
}

fun Animator.addIdlingListener(): Animator = apply {
    addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) = getIdling().start(ANIM_TAG)
        override fun onAnimationEnd(animation: Animator) = getIdling().stop(ANIM_TAG)
        override fun onAnimationCancel(animation: Animator) = getIdling().stop(ANIM_TAG)
        override fun onAnimationPause(animation: Animator) = getIdling().stop(ANIM_TAG)
        override fun onAnimationResume(animation: Animator) = getIdling().start(ANIM_TAG)
    })
}

//endregion