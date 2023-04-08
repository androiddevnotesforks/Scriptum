package sgtmelon.scriptum.infrastructure.adapter.parent

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback

/**
 * More stylish version of [ListAdapter] :D.
 * There not available custom notify calls (like it does in [ParentManualAdapter]).
 */
abstract class ParentListAdapter<T, VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffCallback),
    Adapter.Simple<T> {

    override fun notifyList(list: List<T>) = submitList(getListCopy(list))

    /**
     * Need copy list inside implementation. Because if you pass same list (without copy) and
     * update some data - data also will be changed inside your adapter. And when you will try
     * submit it (notify adapter) - nothing will happen, because inside your Adapter will be
     * exactly the same list (with no changes).
     */
    abstract fun getListCopy(list: List<T>): List<T>

    override fun onViewRecycled(holder: VH) {
        super.onViewRecycled(holder)
        (holder as? UnbindCallback)?.unbind()
    }
}