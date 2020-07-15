package sgtmelon.scriptum.presentation.screen.vm.callback.note

import sgtmelon.scriptum.presentation.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.presentation.control.touch.RollTouchControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.IRollNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.note.RollNoteViewModel

/**
 * Interface for communication [IRollNoteFragment] with [RollNoteViewModel].
 */
interface IRollNoteViewModel : IParentNoteViewModel,
        RollWriteHolder.Callback,
        RollTouchControl.Callback {

    fun onClickVisible()

    fun onUpdateInfo()

    fun onEditorClick(i: Int): Boolean

    fun onClickAdd(simpleClick: Boolean)

    fun onClickItemCheck(p: Int)

    fun onLongClickItemCheck()

}