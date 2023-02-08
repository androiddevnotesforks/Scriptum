package sgtmelon.scriptum.infrastructure.screen.parent.list

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentDiffAdapter
import sgtmelon.scriptum.infrastructure.model.state.UpdateListState
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerInsertScroll

/**
 * Needed for catch custom [adapter] updates.
 */
interface CustomListNotifyUi<T> {

    val viewModel: CustomListNotifyViewModel<T>

    val adapter: ParentDiffAdapter<T, *>

    val layoutManager: LinearLayoutManager

    val recyclerView: RecyclerView?

    /**
     * Use here [UpdateListState.NotifyHard] case, because it will prevent lags during
     * insert first item. When empty info hides and list appears. Insert animation
     * and list fade in animation concurrent with each other and it's looks laggy.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun catchListUpdate(list: List<T>) {
        when (val state = viewModel.updateList) {
            is UpdateListState.Set -> adapter.setList(list)
            is UpdateListState.Notify -> adapter.notifyList(list)
            is UpdateListState.NotifyHard -> adapter.setList(list).notifyDataSetChanged()
            is UpdateListState.Remove -> adapter.setList(list).notifyItemRemoved(state.p)
            is UpdateListState.Insert -> {
                adapter.setList(list).notifyItemInserted(state.p)
                RecyclerInsertScroll(recyclerView, layoutManager).scroll(list, state.p)
            }
            is UpdateListState.Move -> adapter.setList(list).notifyItemMoved(state.from, state.to)
        }
    }
}