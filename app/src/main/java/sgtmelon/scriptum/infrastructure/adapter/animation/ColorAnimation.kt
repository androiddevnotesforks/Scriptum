package sgtmelon.scriptum.infrastructure.adapter.animation

import android.view.animation.AccelerateDecelerateInterpolator
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ItemColorBinding
import sgtmelon.test.idling.getWaitIdling

class ColorAnimation {

    inline fun startCheckFade(binding: ItemColorBinding, changeUi: () -> Unit) {
        val duration = binding.root.context.resources.getInteger(R.integer.color_fade_time).toLong()

        val transition = Fade()
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .addTarget(binding.checkImage)

        TransitionManager.beginDelayedTransition(binding.parentContainer, transition)

        getWaitIdling().start(duration)
        changeUi()
    }
}