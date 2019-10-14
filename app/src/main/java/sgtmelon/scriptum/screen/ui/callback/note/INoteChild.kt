package sgtmelon.scriptum.screen.ui.callback.note

import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.vm.note.NoteViewModel
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel

/**
 * Interface for communication [TextNoteViewModel] и [RollNoteViewModel] with [NoteActivity]
 */
interface INoteChild {

    /**
     * After save new note need update [NoteViewModel.id]
     */
    fun onUpdateNoteId(id: Long)

    fun onConvertNote()

    fun finish()

}