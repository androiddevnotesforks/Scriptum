package sgtmelon.scriptum.screen.vm.callback.note

import android.os.Bundle
import sgtmelon.scriptum.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.control.input.watcher.InputTextWatcher
import sgtmelon.scriptum.control.touch.RollTouchControl
import sgtmelon.scriptum.screen.ui.callback.note.roll.IRollNoteMenu
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel
import java.util.*

/**
 * Interface for communication [RollNoteFragment] with [RollNoteViewModel]
 *
 * @author SerjantArbuz
 */
interface IRollNoteViewModel : IParentViewModel,
        IRollNoteMenu,
        InputTextWatcher.TextChange,
        RollWriteHolder.RollChange,
        RollTouchControl.Result {

    fun onSaveData(bundle: Bundle)

    fun onPause()

    fun onUpdateData()

    fun onClickBackArrow()

    fun onPressBack(): Boolean

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

    fun onCancelNoteBind()

}