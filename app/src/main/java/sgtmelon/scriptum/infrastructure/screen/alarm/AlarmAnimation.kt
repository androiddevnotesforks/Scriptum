package sgtmelon.scriptum.infrastructure.screen.alarm

import android.animation.AnimatorSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.getAlphaAnimator
import sgtmelon.scriptum.infrastructure.utils.getAlphaInterpolator
import sgtmelon.test.idling.addIdlingListener

class AlarmAnimation {

    inline fun startLogoShiftAnimation(
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

    fun startContentAnimation(recyclerView: View?, buttonContainer: View?) {
        if (recyclerView == null || buttonContainer == null) return

        val resources = recyclerView.context.resources

        AnimatorSet().apply {
            interpolator = getAlphaInterpolator(isVisible = true)
            startDelay = resources.getInteger(R.integer.alarm_show_delay).toLong()
            duration = resources.getInteger(R.integer.alarm_show_time).toLong()

            playTogether(
                getAlphaAnimator(recyclerView, alphaTo = 1f),
                getAlphaAnimator(buttonContainer, alphaTo = 1f)
            )

            addIdlingListener()
        }.start()
    }
}