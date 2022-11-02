package sgtmelon.scriptum.infrastructure.screen.main.bin

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.state.ShowListState

interface BinViewModel {

    val showList: LiveData<ShowListState>

    val itemList: LiveData<List<NoteItem>>

    fun clearRecyclerBin()

    fun restoreNote(p: Int)

    fun getCopyText(p: Int): Flow<String>

    fun clearNote(p: Int)

    //    fun onUpdateData()
    //
    //    fun onClickClearBin()
    //
    //    @Deprecated("Move preparation before show dialog inside some delegator, which will call from UI")
    //    fun onShowOptionsDialog(item: NoteItem, p: Int)
    //
    //    fun onResultOptionsDialog(p: Int, @Options.Bin which: Int)

}