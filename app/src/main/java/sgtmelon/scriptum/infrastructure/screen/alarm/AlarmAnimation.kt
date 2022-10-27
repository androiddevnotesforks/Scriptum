package sgtmelon.scriptum.infrastructure.screen.alarm

import android.animation.AnimatorSet
import android.view.animation.AccelerateInterpolator
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ActivityAlarmBinding
import sgtmelon.scriptum.infrastructure.utils.getAlphaAnimator
import sgtmelon.scriptum.infrastructure.utils.getAlphaInterpolator
import sgtmelon.test.idling.addIdlingListener

class AlarmAnimation {

    inline fun startLogoTransition(
        binding: ActivityAlarmBinding?,
        crossinline onEnd: () -> Unit,
        changeUi: () -> Unit
    ) {
        if (binding == null) return

        val transition = AutoTransition()
            .setInterpolator(AccelerateInterpolator())
            .addTarget(binding.logoView)
            .addIdlingListener()
            .addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) = onEnd()
            })

        TransitionManager.beginDelayedTransition(binding.parentContainer, transition)

        changeUi()
    }

    fun startContentFade(binding: ActivityAlarmBinding?) {
        if (binding == null) return

        val resources = binding.recyclerView.context.resources

        AnimatorSet().apply {
            interpolator = getAlphaInterpolator(isVisible = true)
            startDelay = resources.getInteger(R.integer.alarm_show_delay).toLong()
            duration = resources.getInteger(R.integer.alarm_show_time).toLong()

            playTogether(
                getAlphaAnimator(binding.recyclerView, alphaTo = 1f),
                getAlphaAnimator(binding.buttonContainer, alphaTo = 1f)
            )

            addIdlingListener()
        }.start()
    }
}