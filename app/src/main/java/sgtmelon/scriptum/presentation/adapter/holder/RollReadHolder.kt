package sgtmelon.scriptum.presentation.adapter.holder

import android.view.View
import android.widget.CheckBox
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.presentation.adapter.RollAdapter
import sgtmelon.scriptum.presentation.listener.ItemListener

/**
 * Holder of note roll row read state, use in [RollAdapter].
 */
class RollReadHolder(
    private val binding: ItemRollReadBinding,
    private val clickListener: ItemListener.ActionClick,
    private val longClickListener: ItemListener.LongClick
) : ParentHolder(binding.root) {

    /**
     * Button which displays above [rollCheck] for ripple effect on click
     */
    private val clickView: View = itemView.findViewById(R.id.roll_read_click_button)
    private val rollCheck: CheckBox = itemView.findViewById(R.id.roll_read_check)

    init {
        clickView.setOnClickListener { v ->
            checkNoPosition { clickListener.onItemClick(v, it) { rollCheck.toggle() } }
        }
        clickView.setOnLongClickListener { v ->
            checkNoPosition { longClickListener.onItemLongClick(v, it) }
        }
    }

    fun bind(item: RollItem, noteState: NoteState?, isToggleCheck: Boolean) = binding.apply {
        this.item = item
        this.isBin = noteState?.isBin == true
        this.isToggleCheck = isToggleCheck
    }.executePendingBindings()

}