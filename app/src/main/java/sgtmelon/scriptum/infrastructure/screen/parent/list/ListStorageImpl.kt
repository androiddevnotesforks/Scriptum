package sgtmelon.scriptum.infrastructure.screen.parent.list

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sgtmelon.extensions.runMain
import sgtmelon.scriptum.cleanup.extension.move
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState
import sgtmelon.scriptum.infrastructure.utils.delegators.ResetValueDelegator
import sgtmelon.scriptum.infrastructure.utils.extensions.removeAtOrNull
import sgtmelon.test.idling.getIdling

/**
 * Class help work with: list screens, custom adapter notify calls, display of UI elements.
 */
class ListStorageImpl<T>(
    val changeTag: String,
    val nextTag: String
) : ListStorage<T> {

    override val show: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)

    override val data: MutableLiveData<List<T>> = MutableLiveData()

    /** Local storage for [T] items, because don't want put mutable list inside [data]. */
    val localData: MutableList<T> = mutableListOf()

    fun notifyShow() {
        val newState = if (localData.isEmpty()) ShowListState.Empty else ShowListState.List
        notifyShow(newState)
    }

    fun notifyShow(state: ShowListState) {
        val currentState = show.value ?: return

        /** Skip same state. */
        if (currentState != state) {
            show.postValue(state)
        }
    }

    inline fun <V> change(update: UpdateListState? = null, func: (MutableList<T>) -> V): V {
        getIdling().start(changeTag)

        val value = func(localData)

        if (update != null) {
            this.update = update
        }

        data.postValue(localData)
        notifyShow()

        getIdling().stop(changeTag)

        return value
    }

    /**
     * Set [data] value on mainThread needed for creating a queue of values, which must be posted.
     * Check [LiveData.postValue] documentation to get deeper.
     */
    suspend inline fun <V> changeNext(
        update: UpdateListState? = null,
        crossinline func: suspend (MutableList<T>) -> V
    ): V {
        getIdling().start(nextTag)

        val value = func(localData)

        if (update != null) {
            this.update = update
        }

        runMain { data.value = localData }
        notifyShow()

        getIdling().stop(nextTag)

        return value
    }

    /**
     * Important: don't use [MutableLiveData.postValue] here with [data], because it
     * leads to UI glitches (during item drag/move).
     */
    @MainThread
    fun move(from: Int, to: Int) {
        localData.move(from, to)
        update = UpdateListState.Move(from, to)
        data.value = localData
    }

    /**
     * Important: don't use [MutableLiveData.postValue] here with [data], because it
     * leads to UI glitches (during item swipe).
     */
    @MainThread
    fun swipe(position: Int): T? {
        val item = localData.removeAtOrNull(position) ?: return null

        update = UpdateListState.Remove(position)
        data.value = localData
        notifyShow()

        return item
    }

    override var update by ResetValueDelegator<UpdateListState>(UpdateListState.Notify)

}