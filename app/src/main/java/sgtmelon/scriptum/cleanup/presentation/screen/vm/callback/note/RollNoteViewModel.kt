package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note

import sgtmelon.scriptum.cleanup.presentation.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.cleanup.presentation.control.touch.RollTouchControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.RollNoteViewModelImpl

/**
 * Interface for communication [RollNoteFragment] with [RollNoteViewModelImpl].
 */
interface RollNoteViewModel : ParentNoteViewModel,
    RollWriteHolder.Callback,
    RollTouchControl.Callback {

    fun onClickVisible()

    fun onUpdateInfo()

    fun onEditorClick(i: Int): Boolean

    fun onClickAdd(simpleClick: Boolean)

    fun onClickItemCheck(p: Int)
}