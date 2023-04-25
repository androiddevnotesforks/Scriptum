package sgtmelon.scriptum.infrastructure.screen.main.bin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import sgtmelon.extensions.flowIO
import sgtmelon.extensions.launchIO
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.useCase.main.ClearBinUseCase
import sgtmelon.scriptum.domain.useCase.main.GetBinListUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
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
        viewModelScope.launchIO {
            list.change { it.clearAdd(getList()) }
        }
    }

    override fun clearRecyclerBin() {
        viewModelScope.launchIO { clearBin() }
        list.change { it.clear() }
    }

    override fun restoreNote(p: Int) {
        val item = list.change { it.removeAtOrNull(p) } ?: return
        viewModelScope.launchIO { restoreNote(item) }
    }

    override fun getNoteText(p: Int): Flow<String> = flowIO {
        val item = list.localData.getOrNull(p) ?: return@flowIO
        emit(getCopyText(item))
    }

    override fun clearNote(p: Int) {
        val item = list.change { it.removeAtOrNull(p) } ?: return
        viewModelScope.launchIO { clearNote(item) }
    }
}