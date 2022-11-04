package sgtmelon.scriptum.infrastructure.screen.main.bin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowOnBack
import sgtmelon.extensions.launchBack
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.useCase.main.ClearBinUseCase
import sgtmelon.scriptum.domain.useCase.main.GetBinListUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsViewModelImpl
import sgtmelon.scriptum.infrastructure.utils.clearAdd
import sgtmelon.scriptum.infrastructure.utils.removeAtOrNull
import sgtmelon.test.idling.getIdling

class BinViewModelImpl(
    private val getList: GetBinListUseCase,
    private val getCopyText: GetCopyTextUseCase,
    private val restoreNote: RestoreNoteUseCase,
    private val clearBin: ClearBinUseCase,
    private val clearNote: ClearNoteUseCase
) : ViewModel(),
    BinViewModel {

    override val showList: MutableLiveData<ShowListState> = MutableLiveData(ShowListState.Loading)

    /**
     * There is no reason check current [showList] state for skip identical values (like it done
     * for [NotificationsViewModelImpl.showList]). Because here we only can remove items from
     * list, without ability to undo this action.
     */
    private fun notifyShowList() {
        showList.postValue(if (_itemList.isEmpty()) ShowListState.Empty else ShowListState.List)
    }

    override val itemList: MutableLiveData<List<NoteItem>> = MutableLiveData()

    /** This list needed because don't want put mutable list inside liveData. */
    private val _itemList: MutableList<NoteItem> = mutableListOf()

    override fun updateData() {
        viewModelScope.launchBack {
            getIdling().start(IdlingTag.Bin.LOAD_DATA)

            showList.postValue(ShowListState.Loading)
            _itemList.clearAdd(getList())
            itemList.postValue(_itemList)
            notifyShowList()

            getIdling().stop(IdlingTag.Bin.LOAD_DATA)
        }
    }

    override fun clearRecyclerBin() {
        viewModelScope.launchBack { clearBin() }

        _itemList.clear()
        itemList.postValue(_itemList)
        notifyShowList()
    }

    override fun restoreNote(p: Int) {
        val item = _itemList.removeAtOrNull(p) ?: return

        viewModelScope.launchBack { restoreNote(item) }

        itemList.postValue(_itemList)
        notifyShowList()
    }

    override fun getNoteText(p: Int): Flow<String> = flowOnBack {
        val item = _itemList.getOrNull(p) ?: return@flowOnBack
        emit(getCopyText(item))
    }

    override fun clearNote(p: Int) {
        val item = _itemList.removeAtOrNull(p) ?: return

        viewModelScope.launchBack { clearNote(item) }

        itemList.postValue(_itemList)
        notifyShowList()
    }
}