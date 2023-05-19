package sgtmelon.scriptum.infrastructure.adapter.parent

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.parent.Adapter.Manual.Callback
import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState
import sgtmelon.scriptum.infrastructure.utils.extensions.clearAdd

/**
 * Version of [RecyclerView.Adapter] + diffUtils, but with ability to notify items by yourself.
 * Sometimes it's needed to skip some diff calculations.
 *
 * For example, you need update data inside [list], but without item notify, because it's already
 * updated inside [RecyclerView.ViewHolder] during action (e.g. click, long click). And this
 * class make a deal.
 */
abstract class ParentManualAdapter<T, VH : RecyclerView.ViewHolder>(
    private val diff: ParentManualDiff<T>
) : RecyclerView.Adapter<VH>(),
    Adapter.Manual<T> {

    private val list: MutableList<T> = ArrayList()

    override fun notifyList(list: List<T>) {
        setList(list)

        DiffUtil.calculateDiff(diff)
            .dispatchUpdatesTo(this)
    }

    override fun notifyList(list: List<T>, state: UpdateListState, callback: Callback) {
        when (state) {
            is UpdateListState.Set -> setList(list)
            is UpdateListState.Notify -> notifyList(list)
            is UpdateListState.Change -> setList(list).notifyItemChanged(state.p)
            is UpdateListState.Remove -> setList(list).notifyItemRemoved(state.p)
            is UpdateListState.Insert -> {
                setList(list).notifyItemInserted(state.p)
                callback.scrollToInsert(state.p)
            }
            is UpdateListState.Move -> setList(list).notifyItemMoved(state.from, state.to)
        }
    }

    private fun setList(list: List<T>) = apply {
        diff.setList(this.list, list)
        this.list.clearAdd(getListCopy(list))
    }


    protected fun getItem(position: Int): T = list[position]

    override fun getItemCount() = list.size

    override fun onViewRecycled(holder: VH) {
        super.onViewRecycled(holder)
        (holder as? UnbindCallback)?.unbind()
    }
}