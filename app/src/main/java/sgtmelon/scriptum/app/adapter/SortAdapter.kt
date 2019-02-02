package sgtmelon.scriptum.app.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.holder.SortHolder
import sgtmelon.scriptum.app.model.item.SortItem
import sgtmelon.scriptum.databinding.ItemSortBinding
import sgtmelon.scriptum.element.SortDialog
import sgtmelon.scriptum.office.intf.ItemIntf
import sgtmelon.scriptum.office.st.SortSt

/**
 * Адаптер для [SortDialog]
 */
class SortAdapter(context: Context, clickListener: ItemIntf.ClickListener)
    : ParentAdapter<SortItem, SortHolder>(context, clickListener) {

    val sortSt = SortSt()

    override fun setList(list: List<SortItem>) {
        super.setList(list)
        sortSt.updateEnd(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortHolder {
        val binding = DataBindingUtil.inflate<ItemSortBinding>(
                inflater, R.layout.item_sort, parent, false
        )

        return SortHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: SortHolder, position: Int) {
        holder.bind(list[position], position, sortSt.end)
    }

}