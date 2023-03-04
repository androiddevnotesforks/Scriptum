package sgtmelon.scriptum.develop.infrastructure.screen.print

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sgtmelon.extensions.launchBack
import sgtmelon.scriptum.develop.domain.GetPrintListUseCase
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.develop.infrastructure.model.PrintType
import sgtmelon.scriptum.infrastructure.model.state.ShowListState

class PrintDevelopViewModelImpl(
    type: PrintType?,
    private val getList: GetPrintListUseCase
) : ViewModel(),
    PrintDevelopViewModel {

    override val showList: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)

    override val itemList: MutableLiveData<List<PrintItem>> = MutableLiveData()

    init {
        if (type != null) {
            viewModelScope.launchBack { fetchList(type) }
        } else {
            // TODO #ERROR_HANDLER post error (if type == null) to live data and catch it in parent activity/fragment
        }
    }

    private suspend fun fetchList(type: PrintType) {
        showList.postValue(ShowListState.Loading)

        val list = getList(type)
        val state = if (list.isEmpty()) ShowListState.Empty else ShowListState.List

        itemList.postValue(list)
        showList.postValue(state)
    }
}