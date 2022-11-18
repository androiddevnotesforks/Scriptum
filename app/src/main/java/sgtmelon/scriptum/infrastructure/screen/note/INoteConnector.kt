package sgtmelon.scriptum.infrastructure.screen.note

import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.IRollNoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Interface for communication [ITextNoteViewModel] or [IRollNoteViewModel] with [NoteActivity].
 */
interface INoteConnector {

    /**
     * After save new note need update [NoteViewModel.id]
     */
    fun onUpdateNoteId(id: Long)

    /**
     * After save note need update [NoteViewModel.color]
     */
    fun onUpdateNoteColor(color: Color)

    fun onConvertNote()

    fun isOrientationChanging(): Boolean


    fun finish()

}