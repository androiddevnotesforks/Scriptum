package sgtmelon.scriptum.screen.vm.callback.main

import sgtmelon.scriptum.presentation.receiver.MainReceiver
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.screen.vm.main.NotesViewModel
import java.util.*

/**
 * Interface for communication [NotesFragment] with [NotesViewModel]
 */
interface INotesViewModel : IParentViewModel, MainReceiver.Callback {

    fun onUpdateData()


    fun onClickNote(p: Int)

    fun onShowOptionsDialog(p: Int)


    fun onResultOptionsDialog(p: Int, which: Int)

    fun onResultDateDialog(calendar: Calendar, p: Int)

    fun onResultDateDialogClear(p: Int)

    fun onResultTimeDialog(calendar: Calendar, p: Int)

}