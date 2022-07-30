package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main

import java.util.Calendar
import sgtmelon.scriptum.cleanup.domain.model.annotation.Options
import sgtmelon.scriptum.cleanup.presentation.receiver.screen.MainScreenReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.NotesViewModel

/**
 * Interface for communication [INotesFragment] with [NotesViewModel].
 */
interface INotesViewModel : IParentViewModel,
    MainScreenReceiver.BindCallback,
    MainScreenReceiver.AlarmCallback {

    fun onUpdateData()


    fun onClickNote(p: Int)

    fun onShowOptionsDialog(p: Int)


    fun onResultOptionsDialog(p: Int, @Options.Notes which: Int)

    fun onResultDateDialog(calendar: Calendar, p: Int)

    fun onResultDateDialogClear(p: Int)

    fun onResultTimeDialog(calendar: Calendar, p: Int)

}