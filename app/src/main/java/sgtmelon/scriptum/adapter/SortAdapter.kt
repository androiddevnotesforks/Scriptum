package sgtmelon.scriptum.adapter

import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.SortHolder
import sgtmelon.scriptum.dialog.SortDialog
import sgtmelon.scriptum.model.item.SortItem
import sgtmelon.scriptum.model.state.SortState
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.inflateBinding


/**
 * Адаптер для [SortDialog]
 *
 * @author SerjantArbuz
 */
class SortAdapter(private val clickListener: ItemListener.Click)
    : ParentAdapter<SortItem, SortHolder>() {

    val sortState = SortState()

    override fun setList(list: List<SortItem>) {
        super.setList(list)
        sortState.updateEnd(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            SortHolder(parent.inflateBinding(R.layout.item_sort), clickListener)

    override fun onBindViewHolder(holder: SortHolder, position: Int) =
            holder.bind(list[position], position, sortState.end)

}