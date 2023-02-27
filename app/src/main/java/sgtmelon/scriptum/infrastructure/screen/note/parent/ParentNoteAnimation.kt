package sgtmelon.scriptum.infrastructure.screen.note.parent

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.view.animation.AccelerateDecelerateInterpolator
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.IncNotePanelBinding
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.utils.extensions.*
import sgtmelon.test.idling.getWaitIdling

/**
 * Current state of [isEdit]/[state] needed for skip animation during note open.
 */
class ParentNoteAnimation(
    private var isEdit: Boolean,
    private var state: NoteState
) {

    private var animator: Animator? = null

    fun startPanelFade(
        binding: IncNotePanelBinding?,
        isEdit: Boolean,
        state: NoteState
    ) {
        if (binding == null) return

        if (this.isEdit == isEdit && this.state == state) {
            /** Skip setup if was double call and animation already running. */
            if (animator?.isRunning.isTrue()) return

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

    private fun changePanel(
        binding: IncNotePanelBinding,
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

    private fun panelFade(binding: IncNotePanelBinding, isEdit: Boolean, state: NoteState) {
        val resources = binding.root.context.resources
        val duration = resources.getInteger(R.integer.note_panel_change_time).toLong()

        animator = AnimatorSet().apply {
            this.duration = duration
            this.interpolator = AccelerateDecelerateInterpolator()

            val isBin = state == NoteState.DELETE
            playTogether(with(binding) {
                listOfNotNull(
                    getAlphaAnimator(binContainer, visibleTo = isBin),
                    getAlphaAnimator(editContainer, visibleTo = !isBin && isEdit),
                    getAlphaAnimator(readContainer, visibleTo = !isBin && !isEdit)
                )
            })

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    animator = null
                    /** Call it because some views are visible, but with zero alpha channel. */
                    changePanel(binding, isEdit, state)
                }
            })

            start()
        }

        getWaitIdling().start(duration)
    }
}