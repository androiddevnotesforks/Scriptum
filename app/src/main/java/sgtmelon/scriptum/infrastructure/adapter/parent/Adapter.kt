package sgtmelon.scriptum.infrastructure.adapter.parent

import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState

/**
 * Need copy list inside implementation. Because if you pass same list (without copy) and update
 * some data - data also will be changed inside your adapter. And when you will try
 * submit it (notify adapter) - nothing will happen, because inside your Adapter will be
 * exactly the same list (with no changes).
 */
sealed interface Adapter<T> {

    fun notifyList(list: List<T>)

    interface Simple<T> : Adapter<T>

    interface Custom<T> : Adapter<T> {

        fun notifyList(list: List<T>, state: UpdateListState, callback: Callback)

        interface Callback {
            fun scrollToInsert(position: Int)
        }
    }
}