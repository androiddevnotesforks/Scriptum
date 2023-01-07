package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.bindTextColor
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf

/**
 * Holder of note roll row read state, use in [RollAdapter].
 */
class RollReadHolder(
    private val binding: ItemRollReadBinding,
    private val clickListener: ItemListener.ActionClick
) : ParentHolder(binding.root),
    UnbindCallback {

    init {
        binding.clickButton.setOnClickListener { v ->
            checkPosition { clickListener.onItemClick(v, it) { binding.checkBox.toggle() } }
        }
    }

    override fun unbind() {
        binding.clickButton.setOnClickListener(null)
    }

    fun bind(item: RollItem, state: NoteState?) = with(binding) {
        checkBox.isChecked = item.isCheck

        val checkDescription = if (item.isCheck) {
            context.getString(R.string.description_item_roll_uncheck, item.text)
        } else {
            context.getString(R.string.description_item_roll_check, item.text)
        }
        clickButton.contentDescription = checkDescription
        clickButton.makeVisibleIf(condition = state != NoteState.DELETE) { makeInvisible() }

        rollText.text = item.text
        rollText.bindTextColor(!item.isCheck, R.attr.clContent, R.attr.clContrast)
    }
}