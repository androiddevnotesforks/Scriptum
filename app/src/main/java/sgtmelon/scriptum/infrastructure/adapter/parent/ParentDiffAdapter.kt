package sgtmelon.scriptum.infrastructure.adapter.parent

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.parent.Adapter.Custom.Callback
import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState
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
    Adapter.Custom<T> {

    private var diffResult: DiffUtil.DiffResult? = null

    private val list: MutableList<T> = ArrayList()

    fun getItem(position: Int): T = list[position]

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

    /**
     * Use here [UpdateListState.NotifyHard] case, because it will prevent lags during
     * insert first item. When empty info hides and list appears. Insert animation
     * and list fade in animation concurrent with each other and it's looks laggy.
     *
     * TODO remove/fix it in future
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun notifyList(list: List<T>, state: UpdateListState, callback: Callback) {
        when (state) {
            is UpdateListState.Set -> setList(list)
            is UpdateListState.Notify -> notifyList(list)
            is UpdateListState.NotifyHard -> setList(list).notifyDataSetChanged()
            is UpdateListState.Change -> setList(list).notifyItemChanged(state.p)
            is UpdateListState.Remove -> setList(list).notifyItemRemoved(state.p)
            is UpdateListState.Insert -> {
                setList(list).notifyItemInserted(state.p)
                callback.scrollToInsert(state.p)
            }
            is UpdateListState.Move -> setList(list).notifyItemMoved(state.from, state.to)
        }
    }

    abstract fun getListCopy(list: List<T>): List<T>

    override fun getItemCount() = list.size

    override fun onViewRecycled(holder: VH) {
        super.onViewRecycled(holder)
        (holder as? UnbindCallback)?.unbind()
    }
}