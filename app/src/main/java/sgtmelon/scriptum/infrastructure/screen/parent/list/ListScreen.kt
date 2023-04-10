package sgtmelon.scriptum.infrastructure.screen.parent.list

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.infrastructure.adapter.parent.Adapter
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentListAdapter
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentManualAdapter
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

    /**
     * Check first [Adapter.Manual] because [ParentManualAdapter] is instance of
     * [ParentListAdapter] which is instance of [Adapter.Simple].
     *
     * So [ParentManualAdapter] will be checked as [Adapter.Simple] firstly. Instance not only of
     * [Adapter.Manual].
     */
    fun onListUpdate(list: List<T>) = adapter.let {
        when (it) {
            is Adapter.Manual -> it.notifyList(list, viewModel.list.update, callback = this)
            is Adapter.Simple -> it.notifyList(list)
        }
    }

    override fun scrollToInsert(position: Int) = scroll(position)

}