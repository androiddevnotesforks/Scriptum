package sgtmelon.scriptum.infrastructure.screen.note.roll

import androidx.annotation.MainThread
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.infrastructure.adapter.holder.RollHolder
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteViewModel
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListViewModel

interface RollNoteViewModel : ParentNoteViewModel<NoteItem.Roll>,
    ListViewModel<RollItem>,
    RollHolder.WriteCallback {

    fun changeVisible()

    fun changeItemCheck(position: Int)

    fun addItem(toBottom: Boolean, text: String)

    @MainThread
    fun swipeItem(position: Int)

    @MainThread
    fun moveItem(from: Int, to: Int)

    @MainThread
    fun moveItemResult(from: Int, to: Int)

}