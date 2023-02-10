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
    override val itemList: MutableLiveData<List<T>> = MutableLiveData()
    override val _itemList: MutableList<T> = mutableListOf()
    override var updateList: UpdateListState = UpdateListState.Notify
        get() {
            val value = field
            updateList = UpdateListState.Notify
            return value
        }
}