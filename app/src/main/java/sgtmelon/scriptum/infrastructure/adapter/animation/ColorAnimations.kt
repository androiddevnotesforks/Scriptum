package sgtmelon.scriptum.infrastructure.adapter.animation

import android.view.animation.AccelerateDecelerateInterpolator
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ItemColorBinding
import sgtmelon.test.idling.addIdlingListener

class ColorAnimations {

    inline fun prepareCheckAnimation(binding: ItemColorBinding, changeUi: () -> Unit) {
        val duration = binding.root.context.resources.getInteger(R.integer.color_fade_time)

        val transition = Fade()
            .setDuration(duration.toLong())
            .setInterpolator(AccelerateDecelerateInterpolator())
            .addTarget(binding.checkImage)
            .addIdlingListener()

        TransitionManager.beginDelayedTransition(binding.parentContainer, transition)

        changeUi()
    }
}