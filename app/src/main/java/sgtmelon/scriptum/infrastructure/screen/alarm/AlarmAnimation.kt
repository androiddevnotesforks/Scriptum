package sgtmelon.scriptum.infrastructure.screen.alarm

import android.animation.AnimatorSet
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ActivityAlarmBinding
import sgtmelon.scriptum.infrastructure.utils.extensions.getAlphaAnimator
import sgtmelon.scriptum.infrastructure.utils.extensions.getAlphaInterpolator
import sgtmelon.test.idling.getWaitIdling

class AlarmAnimation {

    inline fun startLogoTransition(
        binding: ActivityAlarmBinding?,
        crossinline onEnd: () -> Unit,
        changeUi: () -> Unit
    ) {
        if (binding == null) return

        val resources = binding.parentContainer.context.resources
        val duration = resources.getInteger(R.integer.alarm_start_time).toLong()

        val transition = AutoTransition()
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(duration)
            .addTarget(binding.logoView)
            .addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) = onEnd()
            })

        TransitionManager.beginDelayedTransition(binding.parentContainer, transition)

        changeUi()
    }

    fun startContentFade(binding: ActivityAlarmBinding?) {
        if (binding == null) return

        val resources = binding.recyclerView.context.resources
        val startDelay = resources.getInteger(R.integer.alarm_show_delay).toLong()
        val duration = resources.getInteger(R.integer.alarm_show_time).toLong()

        AnimatorSet().apply {
            this.interpolator = getAlphaInterpolator(isVisible = true)
            this.startDelay = startDelay
            this.duration = duration

            playTogether(
                binding.recyclerView.getAlphaAnimator(alphaTo = 1f),
                binding.buttonContainer.getAlphaAnimator(alphaTo = 1f)
            )
        }.start()

        getWaitIdling().start(waitMillis = startDelay + duration)
    }
}