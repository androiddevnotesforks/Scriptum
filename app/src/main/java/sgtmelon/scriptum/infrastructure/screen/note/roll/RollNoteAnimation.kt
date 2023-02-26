package sgtmelon.scriptum.infrastructure.screen.note.roll

import android.animation.Animator
import sgtmelon.extensions.PERCENT_MAX
import sgtmelon.extensions.PERCENT_MIN
import sgtmelon.extensions.getPercent
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.infrastructure.utils.extensions.ALPHA_MAX
import sgtmelon.scriptum.infrastructure.utils.extensions.ALPHA_MIN
import sgtmelon.scriptum.infrastructure.utils.extensions.animateValue
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.scriptum.infrastructure.utils.extensions.makeGone
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf

/**
 * Current state of [isEdit] mode needed for skip animation during note open.
 */
class RollNoteAnimation(private var isEdit: Boolean) {

    // TODO 1. анимация для отступов главных контейнеров

    // TODO 2. при изменении текста в поле ввода обновлять отступы снизу контейнеров (если
    //         переходит на следующую линию

    private var animator: Animator? = null

    fun startAddPanelChange(binding: FragmentRollNoteBinding, isEdit: Boolean) {
        if (this.isEdit == isEdit) {
            /** Skip setup if was double call and animation already running. */
            if (animator?.isRunning.isTrue()) return

            /** Make it invisible because needed calculated height of container for anim. */
            binding.addPanel.parentContainer.makeVisibleIf(isEdit) { makeInvisible() }
            /** Make it invisible in read state to prevent layout size change. */
            binding.panel.dividerView.makeVisibleIf(isEdit) { makeInvisible() }
            binding.doneProgress.makeVisibleIf(!isEdit)

            return
        }

        this.isEdit = isEdit

        /** Post needed for better UI performance. */
        binding.root.rootView.post {
            addPanelTranslation(binding, isEdit)
        }
    }

    private fun addPanelTranslation(
        binding: FragmentRollNoteBinding,
        isEdit: Boolean
    ) = with(binding) {
        val resources = binding.root.context.resources
        val duration = resources.getInteger(R.integer.note_panel_change_time).toLong()

        val addMaxTranslation = addPanel.parentContainer.height.toFloat()

        onAddPanelTranslationStart(binding, isEdit, addMaxTranslation)

        val from = if (isEdit) PERCENT_MAX else PERCENT_MIN
        val to = if (isEdit) PERCENT_MIN else PERCENT_MAX

        animator = animateValue(from, to, duration, onEnd = {
            animator = null
            onAddPanelTranslationEnd(binding, isEdit)
        }) {
            addPanel.parentContainer.translationY = addMaxTranslation.getPercent(it)
            panel.dividerView.alpha = ALPHA_MAX - ALPHA_MAX.getPercent(it)
        }
    }

    /** This preparation is opposite of changes inside onEnd call - [onAddPanelTranslationEnd]. */
    private fun onAddPanelTranslationStart(
        binding: FragmentRollNoteBinding,
        isEdit: Boolean,
        addMaxTranslation: Float
    ) = with(binding) {
        if (isEdit) {
            /** Make visible but hide add panel before slide in animation. */
            addPanel.parentContainer.makeVisible()
            addPanel.parentContainer.translationY = addMaxTranslation

            /** Make visible but completely transparent for start fade in. */
            panel.dividerView.makeVisible()
            panel.dividerView.alpha = ALPHA_MIN
        } else {
            /**
             * Make visible progress for good look anim of add panel slide out. Progress placed
             * behind, and to animation end it must be already displayed.
             */
            doneProgress.makeVisible()
        }
    }

    private fun onAddPanelTranslationEnd(
        binding: FragmentRollNoteBinding,
        isEdit: Boolean
    ) = with(binding) {
        if (isEdit) {
            /** Add panel is completely slide in, we may hide progress. */
            doneProgress.makeGone()
        } else {
            /**
             * Add panel is completely slide out, may hide it and divider. It's not displayed
             * in screen already: panel is translation to end position, divider has empty alpha.
             */
            addPanel.parentContainer.makeInvisible()
            panel.dividerView.makeInvisible()
        }
    }


    companion object {
        private const val MIN_TRANSLATION = 0
    }
}