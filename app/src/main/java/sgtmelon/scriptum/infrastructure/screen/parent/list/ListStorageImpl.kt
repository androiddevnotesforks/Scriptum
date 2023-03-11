package sgtmelon.scriptum.infrastructure.screen.parent.list

import androidx.lifecycle.MutableLiveData
import sgtmelon.scriptum.cleanup.extension.move
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState
import sgtmelon.scriptum.infrastructure.utils.ResetValueDelegator

/**
 * Class help work with: list screens, custom adapter notify calls, display of UI elements.
 */
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

    inline fun <V> change(update: UpdateListState? = null, func: (MutableList<T>) -> V): V {
        val value = func(localData)

        if (update != null) {
            this.update = update
        }

        data.postValue(localData)
        notifyShow()
        return value
    }

    /**
     * Important: don't use [MutableLiveData.postValue] here with [itemList], because it
     * leads to UI glitches (during item drag/move).
     */
    fun move(from: Int, to: Int) {
        localData.move(from, to)
        update = UpdateListState.Move(from, to)
        data.value = localData
    }

    override var update by ResetValueDelegator<UpdateListState>(UpdateListState.Notify)

}