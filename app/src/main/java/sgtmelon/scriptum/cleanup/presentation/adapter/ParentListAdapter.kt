package sgtmelon.scriptum.cleanup.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.cleanup.presentation.adapter.callback.NotifyAdapterCallback
import sgtmelon.scriptum.cleanup.presentation.adapter.callback.UnbindCallback

/**
 * More stylish version of [ListAdapter] :D
 */
abstract class ParentListAdapter<T, VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffCallback),
    NotifyAdapterCallback<T> {

    override fun notifyList(list: List<T>) = submitList(getListCopy(list))

    abstract fun getListCopy(list: List<T>): List<T>

    override fun onViewRecycled(holder: VH) {
        (holder as? UnbindCallback)?.unbind()
    }
}