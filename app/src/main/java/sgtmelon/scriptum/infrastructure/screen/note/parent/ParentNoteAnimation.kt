package sgtmelon.scriptum.infrastructure.screen.note.parent

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.IncNotePanelBinding
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.utils.extensions.getAlphaAnimator
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.scriptum.infrastructure.utils.extensions.resetAlpha
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

        /**
         * The moment when user save created note. At the same time we have changes of [isEdit]
         * and of [state]. It will cause anim glitch, because [startPanelFade] will be called
         * multiple times.
         *
         * In this situation everything depends on [isEdit] value. Actually, [state] doesn't
         * matter, because it will not affect on UI. Don't need to stop animation if already
         * receive correct data for displaying UI.
         *
         */
        if (this.isEdit == isEdit && this.state == NoteState.CREATE && state == NoteState.EXIST) {
            this.state = state
            return
        }

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

    /** Disable [withAlphaReset] if you need to continue animation from same alpha position. */
    private fun changePanel(
        binding: IncNotePanelBinding,
        isEdit: Boolean,
        state: NoteState,
        withAlphaReset: Boolean = true
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

        /**
         * Reset alpha is important, in case if we skip animation. That means may occur a situation
         * when alpha=0, but view is [View.VISIBLE].
         */
        if (withAlphaReset) {
            binContainer.resetAlpha()
            editContainer.resetAlpha()
            readContainer.resetAlpha()
        }
    }

    /** Prevent fast tapping lags (if animation is still running). */
    private fun stopPreviousAnimation(
        binding: IncNotePanelBinding,
        state: NoteState?,
        isEdit: Boolean?
    ) {
        if (animator == null || state == null || isEdit == null) return

        animator?.removeAllListeners()
        animator?.cancel()
        animator = null

        changePanel(binding, isEdit, state, withAlphaReset = false)
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