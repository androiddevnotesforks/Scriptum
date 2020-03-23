package sgtmelon.scriptum.screen.vm.callback.note

import android.os.Bundle
import sgtmelon.scriptum.presentation.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.control.input.watcher.InputTextWatcher
import sgtmelon.scriptum.control.touch.RollTouchControl
import sgtmelon.scriptum.presentation.receiver.NoteReceiver
import sgtmelon.scriptum.screen.ui.callback.note.INoteMenu
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel
import java.util.*

/**
 * Interface for communication [RollNoteFragment] with [RollNoteViewModel].
 */
interface IRollNoteViewModel : IParentViewModel,
        INoteMenu,
        SaveControl.Callback,
        InputTextWatcher.Callback,
        RollWriteHolder.Callback,
        RollTouchControl.Callback,
        NoteReceiver.Callback {

    fun onSaveData(bundle: Bundle)

    fun onResume()

    fun onPause()


    fun onClickBackArrow()

    fun onPressBack(): Boolean


    fun onClickVisible()

    fun onEditorClick(i: Int): Boolean

    fun onClickAdd(simpleClick: Boolean)

    fun onClickItemCheck(p: Int)

    fun onLongClickItemCheck()


    fun onResultColorDialog(check: Int)

    fun onResultRankDialog(check: Int)

    fun onResultDateDialog(calendar: Calendar)

    fun onResultDateDialogClear()

    fun onResultTimeDialog(calendar: Calendar)

    fun onResultConvertDialog()

}