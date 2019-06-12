package sgtmelon.scriptum.screen.callback.note

import sgtmelon.scriptum.screen.view.note.NoteActivity
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel

/**
 * Интерфейс общения [TextNoteViewModel] и [RollNoteViewModel] с [NoteActivity]
 *
 * @author SerjantArbuz
 */
interface NoteChildCallback {

    /**
     * После сохранения новой заметки необходимо обновить id
     */
    fun onUpdateNoteId(id: Long)

    fun onConvertNote()

    fun finish()

}