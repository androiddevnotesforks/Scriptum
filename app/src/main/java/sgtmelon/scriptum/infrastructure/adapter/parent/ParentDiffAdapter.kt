package sgtmelon.scriptum.infrastructure.adapter.parent

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
    diffCallback: DiffUtil.ItemCallback<T>
) : ParentListAdapter<T, VH>(diffCallback),
    Adapter.Custom<T> {

    private val list: MutableList<T> = ArrayList()

    override fun notifyList(list: List<T>) {
        val newList = getListCopy(list)

        this.list.clearAdd(newList)
        submitList(newList)
    }

    override fun getItem(position: Int): T = list[position]

    override fun getItemCount() = list.size

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

    private fun setList(list: List<T>) = apply { this.list.clearAdd(getListCopy(list)) }

}