package sgtmelon.scriptum.infrastructure.screen.parent.list.notify

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState

/**
 * Facade for [CustomListNotifyViewModel], hide some variable and make some immutable.
 */
interface CustomListNotifyViewModelFacade<T> {

    val showList: LiveData<ShowListState>

    val itemList: LiveData<List<T>>

    val updateList: UpdateListState

}