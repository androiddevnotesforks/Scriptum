package sgtmelon.scriptum.infrastructure.screen.note.roll

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.touch.RollTouchControl
import sgtmelon.scriptum.infrastructure.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteViewModel

/**
 * Interface for communication [RollNoteFragment] with [RollNoteViewModelImpl].
 */
interface RollNoteViewModel : ParentNoteViewModel<NoteItem.Roll>,
    RollWriteHolder.Callback,
    RollTouchControl.Callback {

    fun changeVisible()

    @Deprecated("Постарайся избегать таких обновлений")
    fun onUpdateInfo()

    fun onEditorClick(i: Int): Boolean

    fun onClickAdd(toBottom: Boolean)

    fun onClickItemCheck(p: Int)
}