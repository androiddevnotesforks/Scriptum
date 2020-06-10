package sgtmelon.scriptum.presentation.screen.vm.callback.note

import sgtmelon.scriptum.presentation.control.note.input.watcher.InputTextWatcher
import sgtmelon.scriptum.presentation.receiver.NoteReceiver
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteMenu
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.note.TextNoteViewModel
import java.util.*

/**
 * Interface for communication [TextNoteFragment] with [TextNoteViewModel].
 */
interface ITextNoteViewModel : IParentNoteViewModel,
        INoteMenu,
        InputTextWatcher.Callback,
        NoteReceiver.Callback {

    fun onClickBackArrow()

    fun onPressBack(): Boolean


    fun onResultColorDialog(check: Int)

    fun onResultRankDialog(check: Int)

    fun onResultDateDialog(calendar: Calendar)

    fun onResultDateDialogClear()

    fun onResultTimeDialog(calendar: Calendar)

    fun onResultConvertDialog()

}