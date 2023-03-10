package sgtmelon.scriptum.infrastructure.screen.main.bin

import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.parent.list.notify.CustomListNotifyViewModelFacade

interface BinViewModel : CustomListNotifyViewModelFacade<NoteItem> {

    fun updateData()

    fun clearRecyclerBin()

    fun restoreNote(p: Int)

    fun getNoteText(p: Int): Flow<String>

    fun clearNote(p: Int)

}