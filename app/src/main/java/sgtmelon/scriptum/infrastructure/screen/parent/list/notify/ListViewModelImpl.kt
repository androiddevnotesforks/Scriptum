package sgtmelon.scriptum.infrastructure.screen.parent.list.notify

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState
import sgtmelon.scriptum.infrastructure.utils.ResetValueDelegator

/**
 * ViewModel for specific list updates (when need update not all items).
 */
@Deprecated("Remove")
abstract class ListViewModelImpl<T> : ViewModel(),
    ListViewModel<T> {

    override val showList: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)
    override val itemList: MutableLiveData<List<T>> = MutableLiveData()
    override val _itemList: MutableList<T> = mutableListOf()
    override var updateList by ResetValueDelegator<UpdateListState>(UpdateListState.Notify)
}