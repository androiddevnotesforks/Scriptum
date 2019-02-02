package sgtmelon.scriptum.app.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.SortAdapter
import sgtmelon.scriptum.app.model.item.SortItem
import sgtmelon.scriptum.databinding.ItemSortBinding

/**
 * Держатель сортировки для [SortAdapter]
 */
class SortHolder(private val binding: ItemSortBinding) : RecyclerView.ViewHolder(binding.root) {

    val clickView: View = itemView.findViewById(R.id.click_container)

    fun bind(sortItem: SortItem, position: Int, sortEnd: Int) {
        binding.sortItem = sortItem
        binding.position = position
        binding.sortEnd = sortEnd
        binding.executePendingBindings()
    }

}