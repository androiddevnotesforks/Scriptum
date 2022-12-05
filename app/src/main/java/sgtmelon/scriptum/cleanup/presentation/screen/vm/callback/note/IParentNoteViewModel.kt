package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note

import android.os.Bundle
import java.util.Calendar
import sgtmelon.scriptum.cleanup.presentation.control.note.input.watcher.InputTextWatcher
import sgtmelon.scriptum.cleanup.presentation.control.note.save.SaveControlImpl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.INoteMenu
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.ParentNoteViewModel
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver

/**
 * Parent interface for communicate with children of [ParentNoteViewModel].
 */
interface IParentNoteViewModel : IParentViewModel,
    UnbindNoteReceiver.Callback,
    INoteMenu,
    SaveControlImpl.Callback,
    InputTextWatcher.Callback {

    fun onSaveData(bundle: Bundle)

    fun onResume()

    fun onPause()


    fun onClickBackArrow()

    fun onPressBack(): Boolean


    fun onResultColorDialog(check: Int)

    fun onResultRankDialog(check: Int)

    fun onResultDateDialog(calendar: Calendar)

    fun onResultDateDialogClear()

    fun onResultTimeDialog(calendar: Calendar)

    fun onResultConvertDialog()

}
