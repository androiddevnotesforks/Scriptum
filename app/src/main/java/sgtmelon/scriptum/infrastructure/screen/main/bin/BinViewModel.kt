package sgtmelon.scriptum.infrastructure.screen.main.bin

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.state.ShowListState

interface BinViewModel {

    val showList: LiveData<ShowListState>

    val itemList: LiveData<List<NoteItem>>

    fun updateData()

    fun clearRecyclerBin()

    fun restoreNote(p: Int)

    fun getCopyText(p: Int): Flow<String>

    fun clearNote(p: Int)

}