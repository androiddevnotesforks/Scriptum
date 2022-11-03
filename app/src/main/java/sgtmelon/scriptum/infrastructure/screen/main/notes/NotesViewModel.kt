package sgtmelon.scriptum.infrastructure.screen.main.notes

import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver

interface NotesViewModel : UnbindNoteReceiver.Callback {

    fun updateData()

    // TODO remove
    //    fun onUpdateData()
    //
    //    @Deprecated("Move preparation before show dialog inside some delegator, which will call from UI")
    //    fun onShowOptionsDialog(item: NoteItem, p: Int)
    //
    //
    //    fun onResultOptionsDialog(p: Int, @Options.Notes which: Int)
    //
    //    fun onResultDateDialog(calendar: Calendar, p: Int)
    //
    //    fun onResultDateDialogClear(p: Int)
    //
    //    fun onResultTimeDialog(calendar: Calendar, p: Int)

}