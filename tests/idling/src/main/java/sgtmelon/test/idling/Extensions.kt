package sgtmelon.test.idling

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

@Deprecated("It fails the UI tests")
fun Transition.addIdlingListener(): Transition = apply {
    addListener(object : TransitionListenerAdapter() {
        override fun onTransitionStart(transition: Transition) = getIdling().start(ANIM_TAG)
        override fun onTransitionEnd(transition: Transition) = getIdling().stop(ANIM_TAG)
        override fun onTransitionCancel(transition: Transition) = getIdling().stop(ANIM_TAG)
        override fun onTransitionPause(transition: Transition) = getIdling().stop(ANIM_TAG)
        override fun onTransitionResume(transition: Transition) = getIdling().start(ANIM_TAG)
    })
}

//endregion