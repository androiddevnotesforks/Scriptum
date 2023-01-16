package sgtmelon.scriptum.infrastructure.screen.note.roll

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteViewModel

/**
 * Interface for communication [RollNoteFragment] with [RollNoteViewModelImpl].
 */
interface RollNoteViewModel : ParentNoteViewModel<NoteItem.Roll>,
    RollWriteHolder.Callback {

    fun changeVisible()

    @Deprecated("Постарайся избегать таких обновлений")
    fun onUpdateInfo()

    fun addItem(toBottom: Boolean, text: String)

    fun changeItemCheck(p: Int)
}