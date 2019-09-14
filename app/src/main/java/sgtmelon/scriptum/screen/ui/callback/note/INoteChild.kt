package sgtmelon.scriptum.screen.ui.callback.note

import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel

/**
 * Interface for communication [TextNoteViewModel] и [RollNoteViewModel] with [NoteActivity]
 */
interface INoteChild {

    /**
     * После сохранения новой заметки необходимо обновить id
     */
    fun onUpdateNoteId(id: Long)

    fun onConvertNote()

    fun finish()

}