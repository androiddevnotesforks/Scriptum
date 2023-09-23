package sgtmelon.scriptum.infrastructure.screen.main.bin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowBack
import sgtmelon.extensions.launchBack
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.useCase.main.ClearBinUseCase
import sgtmelon.scriptum.domain.useCase.main.GetBinListUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListStorageImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.clearAdd
import sgtmelon.scriptum.infrastructure.utils.extensions.removeAtOrNull

class BinViewModelImpl(
    override val list: ListStorageImpl<NoteItem>,
    private val getList: GetBinListUseCase,
    private val getCopyText: GetCopyTextUseCase,
    private val restoreNote: RestoreNoteUseCase,
    private val clearBin: ClearBinUseCase,
    private val clearNote: ClearNoteUseCase
) : ViewModel(),
    BinViewModel {

    override fun updateData() {
        viewModelScope.launchBack {
            list.change { it.clearAdd(getList()) }
        }
    }

    override fun clearRecyclerBin() {
        viewModelScope.launchBack { clearBin() }
        list.change { it.clear() }
    }

    override fun restoreNote(p: Int): Flow<NoteItem> = flowBack {
        val item = list.change { it.removeAtOrNull(p) ?: return@flowBack }

        restoreNote(item)
        emit(item)
    }

    override fun getNoteText(p: Int): Flow<String> = flowBack {
        val item = list.localData.getOrNull(p) ?: return@flowBack
        emit(getCopyText(item))
    }

    override fun clearNote(p: Int) {
        val item = list.change { it.removeAtOrNull(p) } ?: return
        viewModelScope.launchBack { clearNote(item) }
    }

    override fun onReceiveInfoChange(state: ShowListState) {
        list.notifyShow(state, withAnimation = false)
    }
}