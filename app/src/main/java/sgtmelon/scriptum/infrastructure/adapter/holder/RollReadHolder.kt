package sgtmelon.scriptum.infrastructure.adapter.holder

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.bindTextColor
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf

/**
 * Holder of roll item in read state.
 */
class RollReadHolder(
    private val binding: ItemRollReadBinding,
    private val callback: Callback
) : ParentHolder(binding.root),
    UnbindCallback {

    fun bind(item: RollItem, state: NoteState) = with(binding) {
        checkBox.isChecked = item.isCheck

        val checkDescription = if (item.isCheck) {
            context.getString(R.string.description_item_roll_uncheck, item.text)
        } else {
            context.getString(R.string.description_item_roll_check, item.text)
        }
        clickButton.contentDescription = checkDescription
        clickButton.makeVisibleIf(condition = state != NoteState.DELETE) { makeInvisible() }

        val animTime = context.resources.getInteger(R.integer.icon_animation_time).toLong()
        clickButton.setOnClickListener { _ ->
            checkPosition {
                callback.onReadCheckClick(it, animTime) { checkBox.toggle() }
            }
        }

        rollText.text = item.text
        rollText.bindTextColor(!item.isCheck, R.attr.clContent, R.attr.clContrast)
    }

    override fun unbind() {
        binding.clickButton.setOnClickListener(null)
    }

    interface Callback {
        fun onReadCheckClick(p: Int, animTime: Long, action: () -> Unit)
    }
}