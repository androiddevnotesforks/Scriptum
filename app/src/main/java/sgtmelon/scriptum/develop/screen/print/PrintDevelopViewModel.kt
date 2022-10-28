package sgtmelon.scriptum.develop.screen.print

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.develop.model.PrintItem
import sgtmelon.scriptum.develop.model.PrintType
import sgtmelon.scriptum.infrastructure.model.state.ShowListState

interface PrintDevelopViewModel {

    fun setup(type: PrintType)

    val showList: LiveData<ShowListState>

    val itemList: LiveData<List<PrintItem>>

}