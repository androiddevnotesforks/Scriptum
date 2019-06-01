package sgtmelon.scriptum.adapter.holder

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.RollAdapter
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.checkNoPosition

/**
 * Держатель пункта списка в состоянии просмотра для [RollAdapter]
 *
 * @author SerjantArbuz
 */
class RollReadHolder(private val binding: ItemRollReadBinding,
                     private val clickListener: ItemListener.Click,
                     private val longClickListener: ItemListener.LongClick
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Кнопка, которая идёт поверх rollCheck, для полноценного эффекта нажатия
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

    fun bind(rollItem: RollItem, noteState: NoteState, checkToggle: Boolean) {
        binding.rollItem = rollItem
        binding.keyBin = noteState.isBin
        binding.checkToggle = checkToggle

        binding.executePendingBindings()
    }

}