package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.view.View
import android.widget.CheckBox
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder
import sgtmelon.scriptum.infrastructure.model.key.NoteState

/**
 * Holder of note roll row read state, use in [RollAdapter].
 */
// TODO add unbind function
class RollReadHolder(
    private val binding: ItemRollReadBinding,
    private val clickListener: ItemListener.ActionClick
) : ParentHolder(binding.root) {

    /**
     * Button which displays above [rollCheck] for ripple effect on click
     */
    private val clickView: View = itemView.findViewById(R.id.roll_read_click_button)
    private val rollCheck: CheckBox = itemView.findViewById(R.id.roll_read_check)

    init {
        clickView.setOnClickListener { v ->
            checkPosition { clickListener.onItemClick(v, it) { rollCheck.toggle() } }
        }
    }

    // TODO remove databinding and use only view binding
    fun bind(item: RollItem, state: NoteState?) = binding.apply {
        this.item = item
        this.isBin = state == NoteState.DELETE
    }.executePendingBindings()

}