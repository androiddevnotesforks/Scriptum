package sgtmelon.scriptum.infrastructure.screen.parent.list.notify

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState

/**
 * ViewModel for specific list updates (when need update not all items).
 */
abstract class ListViewModelImpl<T> : ViewModel(),
    ListViewModel<T> {

    override val showList: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)
    override val itemList: MutableLiveData<List<T>> = MutableLiveData()
    override val _itemList: MutableList<T> = mutableListOf()
    override var updateList: UpdateListState = UpdateListState.Notify
        get() {
            val value = field
            updateList = UpdateListState.Notify
            return value
        }
}