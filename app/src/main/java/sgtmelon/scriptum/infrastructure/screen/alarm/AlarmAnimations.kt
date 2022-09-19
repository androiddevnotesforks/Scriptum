package sgtmelon.scriptum.infrastructure.screen.alarm

import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import sgtmelon.test.idling.addIdlingListener

class AlarmAnimations {

    inline fun startContentAnimation(
        parentContainer: ViewGroup?,
        targetView: View?,
        crossinline onEnd: () -> Unit,
        crossinline changeUi: () -> Unit
    ) {
        if (parentContainer == null || targetView == null) return

        val transition = AutoTransition()
            .setInterpolator(AccelerateInterpolator())
            .addTarget(targetView)
            .addIdlingListener()
            .addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) = onEnd()
            })

        TransitionManager.beginDelayedTransition(parentContainer, transition)

        changeUi()
    }

}