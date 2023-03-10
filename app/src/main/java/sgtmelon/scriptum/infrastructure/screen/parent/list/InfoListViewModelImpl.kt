package sgtmelon.scriptum.infrastructure.screen.parent.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState

/**
 * ViewModel for list with info state. When list is empty -> show info.
 */
abstract class InfoListViewModelImpl<T> : ViewModel(),
    InfoListViewModel<T> {

    override val showList: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)

    /**
     * There is no reason check current [showList] state for skip identical values (like it done
     * for [CustomListNotifyViewModelImpl.showList]). Because here we only can remove items from
     * list, without ability to undo this action.
     */
    protected fun notifyShowList() {
        showList.postValue(if (_itemList.isEmpty()) ShowListState.Empty else ShowListState.List)
    }

    override val itemList: MutableLiveData<List<T>> = MutableLiveData()

    /** Local storage for [T] items, because don't want put mutable list inside [itemList]. */
    protected val _itemList: MutableList<T> = mutableListOf()

}