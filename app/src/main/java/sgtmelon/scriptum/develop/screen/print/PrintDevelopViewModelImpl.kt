package sgtmelon.scriptum.develop.screen.print

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sgtmelon.extensions.launchBack
import sgtmelon.scriptum.develop.model.PrintItem
import sgtmelon.scriptum.develop.model.PrintType
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor
import sgtmelon.scriptum.infrastructure.model.state.ShowListState

class PrintDevelopViewModelImpl(
    type: PrintType,
    private val interactor: DevelopInteractor
) : ViewModel(),
    PrintDevelopViewModel {

    override val showList: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)

    override val itemList: MutableLiveData<List<PrintItem>> = MutableLiveData()

    init {
        viewModelScope.launchBack { fetchList(type) }
    }

    private suspend fun fetchList(type: PrintType) {
        showList.postValue(ShowListState.Loading)

        val list = interactor.getList(type)
        val state = if (list.isEmpty()) ShowListState.Empty else ShowListState.List

        itemList.postValue(list)
        showList.postValue(state)
    }
}