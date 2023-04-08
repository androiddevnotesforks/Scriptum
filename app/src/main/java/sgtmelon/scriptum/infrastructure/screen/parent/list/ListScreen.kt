package sgtmelon.scriptum.infrastructure.screen.parent.list

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.infrastructure.adapter.parent.Adapter
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerInsertScroll

/**
 * Class for catch sealed [adapter] updates.
 */
interface ListScreen<T> : Adapter.Manual.Callback,
    RecyclerInsertScroll {

    val viewModel: ListViewModel<T>

    val adapter: Adapter<T>

    override val layoutManager: LinearLayoutManager
    override val recyclerView: RecyclerView?

    fun onListUpdate(list: List<T>) = adapter.let {
        when (it) {
            is Adapter.Simple -> it.notifyList(list)
            is Adapter.Manual -> it.notifyList(list, viewModel.list.update, callback = this)
        }
    }

    override fun scrollToInsert(position: Int) = scroll(position)

}