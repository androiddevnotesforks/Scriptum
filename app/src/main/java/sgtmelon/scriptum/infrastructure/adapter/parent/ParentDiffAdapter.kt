package sgtmelon.scriptum.infrastructure.adapter.parent

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.infrastructure.adapter.callback.NotifyAdapterCallback
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.utils.extensions.clearAdd

/**
 * Version of [ListAdapter], but with ability to notify items by yourself. Sometimes it's needed
 * to skip some diff calculations.
 *
 * For example, you need update data inside [list], but without item notify, because it's already
 * updated inside [RecyclerView.ViewHolder] during action (e.g. click, long click). And this
 * class make a deal.
 */
abstract class ParentDiffAdapter<T, VH : RecyclerView.ViewHolder>(
    private val diff: ParentDiff<T>
) : RecyclerView.Adapter<VH>(),
    NotifyAdapterCallback<T> {

    private var diffResult: DiffUtil.DiffResult? = null

    private val list: MutableList<T> = ArrayList()

    fun getItem(position: Int): T? = list.getOrNull(position)

    fun setList(list: List<T>): RecyclerView.Adapter<VH> {
        diff.setList(this.list, list)
        diffResult = DiffUtil.calculateDiff(diff)
        this.list.clearAdd(getListCopy(list))
        return this
    }

    override fun notifyList(list: List<T>) {
        setList(list)
        diffResult?.dispatchUpdatesTo(this)
    }

    abstract fun getListCopy(list: List<T>): List<T>

    override fun getItemCount() = list.size

    override fun onViewRecycled(holder: VH) {
        super.onViewRecycled(holder)
        (holder as? UnbindCallback)?.unbind()
    }
}