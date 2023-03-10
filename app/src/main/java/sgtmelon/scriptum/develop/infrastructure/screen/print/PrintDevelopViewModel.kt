package sgtmelon.scriptum.develop.infrastructure.screen.print

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState

interface PrintDevelopViewModel {

    val showList: LiveData<ShowListState>

    val itemList: LiveData<List<PrintItem>>

}