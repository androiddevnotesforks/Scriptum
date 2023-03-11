package sgtmelon.scriptum.infrastructure.screen.parent.list

import androidx.lifecycle.MutableLiveData
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState
import sgtmelon.scriptum.infrastructure.utils.ResetValueDelegator

class ListStorageImpl<T> : ListStorage<T> {

    override val show: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)

    fun notifyShow() {
        val state = show.value ?: return
        val newState = if (localData.isEmpty()) ShowListState.Empty else ShowListState.List

        /** Skip same state. */
        if (state != newState) {
            show.postValue(newState)
        }
    }

    override val data: MutableLiveData<List<T>> = MutableLiveData()

    /** Local storage for [T] items, because don't want put mutable list inside [data]. */
    val localData: MutableList<T> = mutableListOf()

    inline fun <V> change(func: (MutableList<T>) -> V): V {
        val value = func(localData)
        data.postValue(localData)
        notifyShow()
        return value
    }

    inline fun <V> work(func: (MutableList<T>) -> V): V = func(localData)

    override var update by ResetValueDelegator<UpdateListState>(UpdateListState.Notify)

}