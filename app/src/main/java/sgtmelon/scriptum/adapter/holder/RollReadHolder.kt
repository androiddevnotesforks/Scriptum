package sgtmelon.scriptum.adapter.holder

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.RollAdapter
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.extension.checkNoPosition
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.state.NoteState

/**
 * Holder of note roll row read state, use in [RollAdapter].
 */
class RollReadHolder(
        private val binding: ItemRollReadBinding,
        private val clickListener: ItemListener.ActionClick,
        private val longClickListener: ItemListener.LongClick
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Button which displays above [rollCheck] for ripple effect on click
     */
    private val clickView: View = itemView.findViewById(R.id.roll_read_click_button)
    private val rollCheck: CheckBox = itemView.findViewById(R.id.roll_read_check)

    init {
        clickView.apply {
            setOnClickListener {
                checkNoPosition {
                    clickListener.onItemClick(it, adapterPosition) { rollCheck.toggle() }
                }
            }

            setOnLongClickListener {
                checkNoPosition { longClickListener.onItemLongClick(it, adapterPosition) }
                return@setOnLongClickListener true
            }
        }
    }

    fun bind(item: RollItem, noteState: NoteState?, checkToggle: Boolean) = binding.apply {
        this.item = item
        this.keyBin = noteState?.isBin == true
        this.checkToggle = checkToggle
    }.executePendingBindings()

}