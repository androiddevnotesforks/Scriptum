package sgtmelon.scriptum.infrastructure.screen.note.parent

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.IncNotePanelContentBinding
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.test.idling.getIdling

class ParentNoteAnimation {

    inline fun startPanelFade(
        binding: IncNotePanelContentBinding?,
        changeUi: () -> Unit
    ) {
        if (binding == null) return

        getIdling().start(IdlingTag.Alarm.ANIM)

        val resources = binding.root.context.resources
        val duration = resources.getInteger(R.integer.note_panel_change_time).toLong()

        val transition = AutoTransition()
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) {
                    getIdling().stop(IdlingTag.Alarm.ANIM)
                }
            })

        TransitionManager.beginDelayedTransition(binding.parentContainer, transition)

        changeUi()
    }
}