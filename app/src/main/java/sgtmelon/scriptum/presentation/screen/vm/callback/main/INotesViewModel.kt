package sgtmelon.scriptum.presentation.screen.vm.callback.main

import sgtmelon.scriptum.presentation.receiver.screen.MainScreenReceiver
import sgtmelon.scriptum.presentation.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.main.NotesViewModel
import java.util.*

/**
 * Interface for communication [INotesFragment] with [NotesViewModel].
 */
interface INotesViewModel : IParentViewModel,
    MainScreenReceiver.BindCallback,
    MainScreenReceiver.AlarmCallback {

    fun onUpdateData()


    fun onClickNote(p: Int)

    fun onShowOptionsDialog(p: Int)


    fun onResultOptionsDialog(p: Int, which: Int)

    fun onResultDateDialog(calendar: Calendar, p: Int)

    fun onResultDateDialogClear(p: Int)

    fun onResultTimeDialog(calendar: Calendar, p: Int)

}