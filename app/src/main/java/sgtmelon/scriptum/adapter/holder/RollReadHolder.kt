package sgtmelon.scriptum.adapter.holder

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.RollAdapter
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.extension.checkNoPosition
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Holder of note roll row read state, use in [RollAdapter]
 */
class RollReadHolder(private val binding: ItemRollReadBinding,
                     private val clickListener: ItemListener.Click,
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
                    rollCheck.toggle()
                    clickListener.onItemClick(it, adapterPosition)
                }
            }

            setOnLongClickListener {
                checkNoPosition { longClickListener.onItemLongClick(it, adapterPosition) }
                return@setOnLongClickListener true
            }
        }
    }

    fun bind(rollEntity: RollEntity, noteState: NoteState?, checkToggle: Boolean) {
        binding.rollEntity = rollEntity
        binding.keyBin = noteState?.isBin == true
        binding.checkToggle = checkToggle

        binding.executePendingBindings()
    }

}