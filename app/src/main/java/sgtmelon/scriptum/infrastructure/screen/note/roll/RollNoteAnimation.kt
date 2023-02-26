package sgtmelon.scriptum.infrastructure.screen.note.roll

import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.infrastructure.utils.extensions.makeGone
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible
import sgtmelon.scriptum.infrastructure.utils.extensions.updateWithAnimation

class RollNoteAnimation {

    fun startAddPanelTranslation(binding: FragmentRollNoteBinding?, isEdit: Boolean) {
        if (binding == null) return

        if (isEdit) {
            binding.addPanel.parentContainer.apply {
                makeVisible()
                translationY = height.toFloat()
            }
        } else {
            binding.doneProgress.makeVisible()
        }

        val valueFrom = if (isEdit) binding.addPanel.parentContainer.height else 0
        val valueTo = if (isEdit) 0 else binding.addPanel.parentContainer.height

        binding.root.rootView.post {
            updateWithAnimation(200, valueFrom, valueTo, onEnd = {
                if (isEdit) {
                    binding.doneProgress.makeGone()
                    binding.addPanel.parentContainer.translationY = 0f
                } else {
                    binding.addPanel.parentContainer.makeGone()
                }
            }) {
                binding.addPanel.parentContainer.translationY = it.toFloat()
            }
        }
    }
}