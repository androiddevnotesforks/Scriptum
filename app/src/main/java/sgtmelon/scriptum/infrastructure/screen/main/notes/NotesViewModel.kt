package sgtmelon.scriptum.infrastructure.screen.main.notes

import java.util.Calendar
import sgtmelon.scriptum.cleanup.domain.model.annotation.Options
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver

/**
 * Interface for communication [INotesFragment] with [NotesViewModelImpl].
 */
interface NotesViewModel : IParentViewModel,
    UnbindNoteReceiver.Callback {

    fun onUpdateData()

    @Deprecated("Move preparation before show dialog inside some delegator, which will call from UI")
    fun onShowOptionsDialog(item: NoteItem, p: Int)


    fun onResultOptionsDialog(p: Int, @Options.Notes which: Int)

    fun onResultDateDialog(calendar: Calendar, p: Int)

    fun onResultDateDialogClear(p: Int)

    fun onResultTimeDialog(calendar: Calendar, p: Int)

}