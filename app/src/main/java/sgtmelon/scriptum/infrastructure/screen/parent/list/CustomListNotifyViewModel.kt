package sgtmelon.scriptum.infrastructure.screen.parent.list

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.scriptum.infrastructure.model.state.UpdateListState

interface CustomListNotifyViewModel<T> {

    val showList: LiveData<ShowListState>

    val itemList: LiveData<List<T>>

    val updateList: UpdateListState

}