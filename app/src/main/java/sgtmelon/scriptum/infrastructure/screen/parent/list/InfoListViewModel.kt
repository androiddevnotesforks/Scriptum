package sgtmelon.scriptum.infrastructure.screen.parent.list

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.infrastructure.model.state.ShowListState

interface InfoListViewModel<T> {

    val showList: LiveData<ShowListState>

    val itemList: LiveData<List<T>>

}