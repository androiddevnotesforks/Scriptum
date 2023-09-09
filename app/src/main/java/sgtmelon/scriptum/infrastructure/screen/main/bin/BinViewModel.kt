package sgtmelon.scriptum.infrastructure.screen.main.bin

import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.receiver.screen.InfoChangeReceiver
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListViewModel

interface BinViewModel : ListViewModel<NoteItem>,
    InfoChangeReceiver.Callback {

    fun updateData()

    fun clearRecyclerBin()

    fun restoreNote(p: Int): Flow<NoteItem>

    fun getNoteText(p: Int): Flow<String>

    fun clearNote(p: Int)

}