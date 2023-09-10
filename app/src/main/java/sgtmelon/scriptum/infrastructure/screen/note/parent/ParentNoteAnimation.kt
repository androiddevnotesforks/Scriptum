package sgtmelon.scriptum.infrastructure.screen.note.parent

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.view.animation.AccelerateDecelerateInterpolator
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.IncNotePanelBinding
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.utils.extensions.getAlphaAnimator
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.test.idling.getWaitIdling

/**
 * Current state of [isEdit]/[state] needed for skip animation during note open.
 */
class ParentNoteAnimation(
    private var state: NoteState?,
    private var isEdit: Boolean?
) {

    private var animator: Animator? = null

    fun startPanelFade(
        binding: IncNotePanelBinding?,
        isEdit: Boolean,
        state: NoteState
    ) {
        if (binding == null) return

        if (this.state == state && this.isEdit == isEdit) {
            /** Skip setup if was double call and animation already running. */
            if (animator?.isRunning.isTrue()) return

            changePanel(binding, isEdit, state)
            return
        }

        /** Must be called before value update. */
        stopPreviousAnimation(binding, this.state, this.isEdit)

        this.state = state
        this.isEdit = isEdit

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

    /** Prevent fast tapping lags (if animation is still running). */
    private fun stopPreviousAnimation(
        binding: IncNotePanelBinding,
        state: NoteState?,
        isEdit: Boolean?
    ) {
        if (animator != null) {
            animator?.cancel()
            animator = null

            if (state == null || isEdit == null) return

            changePanel(binding, isEdit, state)
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
                    binContainer.getAlphaAnimator(visibleTo = isBin),
                    editContainer.getAlphaAnimator(visibleTo = !isBin && isEdit),
                    readContainer.getAlphaAnimator(visibleTo = !isBin && !isEdit)
                )
            })

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
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