package sgtmelon.scriptum.infrastructure.screen.parent.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.scriptum.infrastructure.model.state.UpdateListState

/**
 * ViewModel for specific list updates (when need update not all items).
 */
abstract class CustomListNotifyViewModelImpl<T> : ViewModel(),
    CustomListNotifyViewModel<T> {

    override val showList: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)

    protected fun notifyShowList() {
        val state = showList.value ?: return
        val newState = if (_itemList.isEmpty()) ShowListState.Empty else ShowListState.List

        /** Skip same state. */
        if (state != newState) {
            showList.postValue(newState)
        }
    }

    override val itemList: MutableLiveData<List<T>> = MutableLiveData()

    /** Local storage for [T] items, because don't want put mutable list inside [itemList]. */
    protected val _itemList: MutableList<T> = mutableListOf()

    /** This variable will be reset after getting value. */
    override var updateList: UpdateListState = UpdateListState.Notify
        get() {
            val value = field
            updateList = UpdateListState.Notify
            return value
        }
}