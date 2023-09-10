package sgtmelon.scriptum.infrastructure.screen.parent.list

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.model.state.list.UpdateListState

interface ListStorage<T> {

    /** Boolean - show list with animation or not. */
    val show: LiveData<Pair<ShowListState, Boolean>>

    val data: LiveData<List<T>>

    val update: UpdateListState

}