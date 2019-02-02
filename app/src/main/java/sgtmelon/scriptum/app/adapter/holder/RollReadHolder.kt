package sgtmelon.scriptum.app.adapter.holder

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.RollAdapter
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.office.st.NoteSt

/**
 * Держатель пункта списка в состоянии просмотра для [RollAdapter]
 */
class RollReadHolder(val binding: ItemRollReadBinding) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Кнопка, которая идёт поверх rollCheck, для полноценного эффекта нажатия
     */
    val clickView: View = itemView.findViewById(R.id.click_button)
    val rollCheck: CheckBox = itemView.findViewById(R.id.roll_check)

    fun bind(rollItem: RollItem, noteSt: NoteSt, checkToggle: Boolean) {
        binding.rollItem = rollItem
        binding.keyBin = noteSt.isBin
        binding.checkToggle = checkToggle

        binding.executePendingBindings()
    }

}