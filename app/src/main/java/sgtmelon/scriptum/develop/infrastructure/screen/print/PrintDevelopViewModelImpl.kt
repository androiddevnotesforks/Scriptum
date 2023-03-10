package sgtmelon.scriptum.develop.infrastructure.screen.print

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import sgtmelon.extensions.launchBack
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.develop.domain.GetPrintListUseCase
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.develop.infrastructure.model.PrintType
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.screen.parent.list.notify.ListViewModelImpl

class PrintDevelopViewModelImpl(
    type: PrintType,
    private val getList: GetPrintListUseCase
) : ListViewModelImpl<PrintItem>(),
    PrintDevelopViewModel {

    override val showList: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)

    override val itemList: MutableLiveData<List<PrintItem>> = MutableLiveData()

    init {
        viewModelScope.launchBack { fetchList(type) }
    }

    private suspend fun fetchList(type: PrintType) {
        val list = getList(type)
        _itemList.clearAdd(list)
        itemList.postValue(list)
        notifyShowList()
    }
}