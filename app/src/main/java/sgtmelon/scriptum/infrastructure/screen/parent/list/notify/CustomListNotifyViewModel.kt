package sgtmelon.scriptum.infrastructure.screen.parent.list.notify

import androidx.lifecycle.MutableLiveData
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState

/**
 * Interface for getting functional without inheritance from [CustomListNotifyViewModelImpl].
 */
interface CustomListNotifyViewModel<T> : CustomListNotifyViewModelFacade<T> {

    override val showList: MutableLiveData<ShowListState>

    fun notifyShowList() {
        val state = showList.value ?: return
        val newState = if (_itemList.isEmpty()) ShowListState.Empty else ShowListState.List

        /** Skip same state. */
        if (state != newState) {
            showList.postValue(newState)
        }
    }

    override val itemList: MutableLiveData<List<T>>

    /** Local storage for [T] items, because don't want put mutable list inside [itemList]. */
    val _itemList: MutableList<T>

    /** This variable will be reset after getting value. */
    override var updateList: UpdateListState

}