package sgtmelon.scriptum.infrastructure.adapter.parent

import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState

sealed interface Adapter<T> {

    fun notifyList(list: List<T>)

    interface Simple<T> : Adapter<T>

    interface Manual<T> : Adapter<T> {

        fun notifyList(list: List<T>, state: UpdateListState, callback: Callback)

        interface Callback {
            fun scrollToInsert(position: Int)
        }
    }
}