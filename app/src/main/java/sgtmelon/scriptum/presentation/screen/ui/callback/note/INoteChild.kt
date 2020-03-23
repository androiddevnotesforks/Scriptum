package sgtmelon.scriptum.presentation.screen.ui.callback.note

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.presentation.screen.ui.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.vm.note.NoteViewModel
import sgtmelon.scriptum.presentation.screen.vm.note.RollNoteViewModel
import sgtmelon.scriptum.presentation.screen.vm.note.TextNoteViewModel

/**
 * Interface for communication [TextNoteViewModel] и [RollNoteViewModel] with [NoteActivity]
 */
interface INoteChild {

    /**
     * After save new note need update [NoteViewModel.id]
     */
    fun onUpdateNoteId(id: Long)

    /**
     * After save note need update [NoteViewModel.color]
     */
    fun onUpdateNoteColor(@Color color: Int)

    fun onConvertNote()

    fun finish()

}