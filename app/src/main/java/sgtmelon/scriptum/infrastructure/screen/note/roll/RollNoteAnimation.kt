package sgtmelon.scriptum.infrastructure.screen.note.roll

import android.animation.Animator
import android.util.Log
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.infrastructure.utils.extensions.*

class RollNoteAnimation {

    /** Current state of isEdit mode. Needed for skip animation during note open. */
    private var isEdit: Boolean? = null

    private var animator: Animator? = null

    fun startAddPanelChange(binding: FragmentRollNoteBinding, isEdit: Boolean) {
        /** Value setup needed to init [isEdit] and skip next isNull case. */
        if (this.isEdit == null) {
            this.isEdit = isEdit
        }

        if (this.isEdit == isEdit) {
            if (animator?.isRunning.isTrue()) return

            Log.i("HERE", "just setup")

            /** Make it invisible in read state to prevent layout size change. */
            binding.panel.dividerView.makeVisibleIf(isEdit) { makeInvisible() }
            binding.addPanel.parentContainer.makeVisibleIf(isEdit)
            binding.doneProgress.makeVisibleIf(!isEdit)
            return
        }

        this.isEdit = isEdit

        addPanelTranslation(binding, isEdit)
    }

    private fun addPanelTranslation(
        binding: FragmentRollNoteBinding,
        isEdit: Boolean
    ) = with(binding) {
        if (isEdit) {
            addPanel.parentContainer.apply {
                makeVisible()
                translationY = height.toFloat()
            }
            panel.dividerView.makeVisible()
        } else {
            doneProgress.makeVisible()
        }

        Log.i("HERE", "anim")

        // TODO у первой анимации (открыл созданную заметку - нажал редактировать) не будет анимации
        // TODO потому что слой ещё не прогрузился и его высота не извесна
        val valueFrom = if (isEdit) addPanel.parentContainer.height else 0
        val valueTo = if (isEdit) 0 else addPanel.parentContainer.height

        root.rootView.post {
            animator = updateWithAnimation(200, valueFrom, valueTo, onEnd = {
                animator = null

                if (isEdit) {
                    doneProgress.makeGone()
                    addPanel.parentContainer.translationY = DEF_TRANSLATION
                } else {
                    addPanel.parentContainer.makeGone()
                    panel.dividerView.makeInvisible()
                }
            }) {
                addPanel.parentContainer.translationY = it.toFloat()
            }
        }
    }

    companion object {
        private const val DEF_TRANSLATION = 0f
    }
}