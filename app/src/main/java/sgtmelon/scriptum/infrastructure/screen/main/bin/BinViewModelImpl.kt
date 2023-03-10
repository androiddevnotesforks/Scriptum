package sgtmelon.scriptum.infrastructure.screen.main.bin

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
import sgtmelon.scriptum.infrastructure.screen.parent.list.notify.CustomListNotifyViewModelImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.clearAdd
import sgtmelon.scriptum.infrastructure.utils.extensions.removeAtOrNull

class BinViewModelImpl(
    private val getList: GetBinListUseCase,
    private val getCopyText: GetCopyTextUseCase,
    private val restoreNote: RestoreNoteUseCase,
    private val clearBin: ClearBinUseCase,
    private val clearNote: ClearNoteUseCase
) : CustomListNotifyViewModelImpl<NoteItem>(),
    BinViewModel {

    override fun updateData() {
        viewModelScope.launchBack {
            _itemList.clearAdd(getList())
            itemList.postValue(_itemList)
            notifyShowList()
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