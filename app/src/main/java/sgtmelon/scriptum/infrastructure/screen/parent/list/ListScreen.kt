package sgtmelon.scriptum.infrastructure.screen.parent.list

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.infrastructure.adapter.parent.Adapter
import sgtmelon.scriptum.infrastructure.screen.parent.list.notify.ListViewModelFacade
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerInsertScroll

/**
 * Class for catch sealed [adapter] updates.
 */
interface ListScreen<T> : Adapter.Custom.Callback,
    RecyclerInsertScroll {

    val viewModel: ListViewModelFacade<T>

    val adapter: Adapter<T>

    override val layoutManager: LinearLayoutManager
    override val recyclerView: RecyclerView?

    fun onListUpdate(list: List<T>) = adapter.let {
        when (it) {
            is Adapter.Simple -> it.notifyList(list)
            is Adapter.Custom -> it.notifyList(list, viewModel.updateList, callback = this)
        }
    }

    override fun scrollToInsert(position: Int) = scroll(position)

}