package sgtmelon.scriptum.app.adapter.holder

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.RollAdapter
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.state.NoteState
import sgtmelon.scriptum.office.utils.AppUtils.checkNoPosition

/**
 * Держатель пункта списка в состоянии просмотра для [RollAdapter]
 */
class RollReadHolder(private val binding: ItemRollReadBinding,
                     private val clickListener: ItemListener.ClickListener
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Кнопка, которая идёт поверх rollCheck, для полноценного эффекта нажатия
     */
    private val clickView: View = itemView.findViewById(R.id.roll_read_click_button)
    private val rollCheck: CheckBox = itemView.findViewById(R.id.roll_read_check)

    init {
        clickView.setOnClickListener { v ->
            checkNoPosition {
                rollCheck.toggle()
                clickListener.onItemClick(v, adapterPosition)
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