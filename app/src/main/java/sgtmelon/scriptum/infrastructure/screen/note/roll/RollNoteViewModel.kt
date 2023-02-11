package sgtmelon.scriptum.infrastructure.screen.note.roll

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.infrastructure.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteViewModel
import sgtmelon.scriptum.infrastructure.screen.parent.list.CustomListNotifyViewModelFacade

interface RollNoteViewModel : ParentNoteViewModel<NoteItem.Roll>,
    CustomListNotifyViewModelFacade<RollItem>,
    RollWriteHolder.Callback {

    fun changeVisible()

    fun changeItemCheck(position: Int)

    fun addItem(toBottom: Boolean, text: String)

    fun swipeItem(position: Int)

    fun moveItem(from: Int, to: Int)

    fun moveItemResult(from: Int, to: Int)

    fun releaseItem(position: Int)

}