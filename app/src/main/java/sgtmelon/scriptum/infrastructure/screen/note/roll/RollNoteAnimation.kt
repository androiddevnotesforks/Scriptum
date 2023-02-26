package sgtmelon.scriptum.infrastructure.screen.note.roll

import android.animation.Animator
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.scriptum.infrastructure.utils.extensions.makeGone
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.scriptum.infrastructure.utils.extensions.updateWithAnimation

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

            /** Make it invisible in read state to prevent layout size change. */
            binding.panel.dividerView.makeVisibleIf(isEdit) { makeInvisible() }
            /** Make it invisible because needed calculated height of container for anim. */
            binding.addPanel.parentContainer.makeVisibleIf(isEdit) { makeInvisible() }
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
        val maxTranslation = addPanel.parentContainer.height

        /** This preparation is opposite of changes inside onEnd call. */
        if (isEdit) {
            addPanel.parentContainer.apply {
                makeVisible()
                translationY = maxTranslation.toFloat()
            }
            panel.dividerView.makeVisible()
        } else {
            doneProgress.makeVisible()
        }

        val resources = binding.root.context.resources
        val duration = resources.getInteger(R.integer.note_panel_change_time).toLong()
        val valueFrom = if (isEdit) maxTranslation else MIN_TRANSLATION
        val valueTo = if (isEdit) MIN_TRANSLATION else maxTranslation

        animator = updateWithAnimation(duration, valueFrom, valueTo, onEnd = {
            animator = null

            if (isEdit) {
                doneProgress.makeGone()
                addPanel.parentContainer.translationY = MIN_TRANSLATION.toFloat()
            } else {
                addPanel.parentContainer.makeInvisible()
                panel.dividerView.makeInvisible()
            }
        }) {
            addPanel.parentContainer.translationY = it.toFloat()

            // TODO add fade for divider (from 0 to 100)
        }
    }

    companion object {
        private const val MIN_TRANSLATION = 0
    }
}