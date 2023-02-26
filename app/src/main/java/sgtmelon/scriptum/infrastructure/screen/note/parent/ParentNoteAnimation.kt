package sgtmelon.scriptum.infrastructure.screen.note.parent

import android.view.animation.AccelerateDecelerateInterpolator
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.IncNotePanelContentBinding
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.test.idling.getIdling

/**
 * Current state of [isEdit]/[state] needed for skip animation during note open.
 */
class ParentNoteAnimation(
    private var isEdit: Boolean,
    private var state: NoteState
) {

    private var isRunning = false

    fun startPanelFade(
        binding: IncNotePanelContentBinding?,
        isEdit: Boolean,
        state: NoteState
    ) {
        if (binding == null) return

        if (this.isEdit == isEdit && this.state == state) {
            /** Skip setup if was double call and animation already running. */
            if (isRunning.isTrue()) return

            changePanel(binding, isEdit, state)
            return
        }

        this.isEdit = isEdit
        this.state = state

        /** Post needed for better UI performance. */
        binding.root.rootView.post {
            panelFade(binding, isEdit, state)
        }
    }

    private fun panelFade(binding: IncNotePanelContentBinding, isEdit: Boolean, state: NoteState) {
        getIdling().start(IdlingTag.Alarm.ANIM)
        isRunning = true

        val resources = binding.root.context.resources
        val duration = resources.getInteger(R.integer.note_panel_change_time).toLong()

        val transition = AutoTransition()
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) {
                    getIdling().stop(IdlingTag.Alarm.ANIM)
                    isRunning = false
                }
            })

        TransitionManager.beginDelayedTransition(binding.parentContainer, transition)

        changePanel(binding, isEdit, state)
    }

    private fun changePanel(
        binding: IncNotePanelContentBinding,
        isEdit: Boolean,
        state: NoteState
    ) = with(binding) {
        if (state == NoteState.DELETE) {
            binContainer.makeVisible()
            editContainer.makeInvisible()
            readContainer.makeInvisible()
        } else {
            binContainer.makeInvisible()
            editContainer.makeVisibleIf(isEdit) { makeInvisible() }
            readContainer.makeVisibleIf(!isEdit) { makeInvisible() }
        }
    }
}