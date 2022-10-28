package sgtmelon.scriptum.develop.screen.print

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sgtmelon.extensions.launchBack
import sgtmelon.scriptum.develop.model.PrintItem
import sgtmelon.scriptum.develop.model.PrintType
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.test.idling.getIdling

class PrintDevelopViewModelImpl(private val interactor: DevelopInteractor) : ViewModel(),
    PrintDevelopViewModel {

    override fun setup(type: PrintType) {
        viewModelScope.launchBack { fetchList(type) }
    }

    override val showList: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)

    override val itemList: MutableLiveData<List<PrintItem>> = MutableLiveData()

    //    override fun onSetup(bundle: Bundle?) {
    //        val typeOrdinal = bundle?.getInt(Intent.TYPE, Default.TYPE) ?: Default.TYPE
    //        val type = PrintType.values().getOrNull(typeOrdinal) ?: return
    //        this.type = type
    //
    //        callback?.setupView(type)
    //        callback?.setupInsets()
    //    }
    //
    //    override fun onSaveData(bundle: Bundle) = with(bundle) {
    //        putInt(Intent.TYPE, type?.ordinal ?: Default.TYPE)
    //    }

    private suspend fun fetchList(type: PrintType) {
        getIdling().start(IdlingTag.Print.LOAD_DATA)

        showList.postValue(ShowListState.Loading)
        val list = interactor.getList(type)
        val state = if (list.isEmpty()) ShowListState.Empty else ShowListState.List
        itemList.postValue(list)
        showList.postValue(state)

        getIdling().stop(IdlingTag.Print.LOAD_DATA)
    }

    //    override fun onUpdateData() {
    //        val type = type ?: return
    //
    //        getIdling().start(IdlingTag.Print.LOAD_DATA)
    //
    //        callback?.beforeLoad()
    //
    //        fun updateList() = callback?.apply {
    //            notifyList(itemList)
    //            onBindingList()
    //        }
    //
    //        /**
    //         * If was rotation need show list. After that fetch updates.
    //         */
    //        if (itemList.isNotEmpty()) {
    //            updateList()
    //        } else {
    //            callback?.showProgress()
    //        }
    //
    //        viewModelScope.launch {
    //            runBack { itemList.clearAdd(interactor.getList(type)) }
    //            updateList()
    //
    //            getIdling().stop(IdlingTag.Print.LOAD_DATA)
    //        }
    //    }
}