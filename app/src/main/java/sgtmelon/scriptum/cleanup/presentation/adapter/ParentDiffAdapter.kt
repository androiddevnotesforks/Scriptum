package sgtmelon.scriptum.cleanup.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.presentation.adapter.diff.ParentDiff
import sgtmelon.scriptum.infrastructure.adapter.callback.NotifyAdapterCallback
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback

/**
 * Version of [ListAdapter], but with ability to notify items by yourself.
 * This class not rely only on [DiffUtil]. Sometimes it's needed to skip some diff calculations.
 */
abstract class ParentDiffAdapter<T, VH : RecyclerView.ViewHolder>(
    private val diff: ParentDiff<T>
) : RecyclerView.Adapter<VH>(),
    NotifyAdapterCallback<T> {

    private var diffResult: DiffUtil.DiffResult? = null

    protected val list: MutableList<T> = ArrayList()

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