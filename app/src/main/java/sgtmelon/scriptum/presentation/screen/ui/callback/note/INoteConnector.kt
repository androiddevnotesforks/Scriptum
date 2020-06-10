package sgtmelon.scriptum.presentation.screen.ui.callback.note

import androidx.annotation.StringRes
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.note.NoteViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.note.RollNoteViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.note.TextNoteViewModel

/**
 * Interface for communication [TextNoteViewModel] or [RollNoteViewModel] with [NoteActivity]
 */
interface INoteConnector {

    /**
     * After save new note need update [NoteViewModel.id]
     */
    fun onUpdateNoteId(id: Long)

    /**
     * After save note need update [NoteViewModel.color]
     */
    fun onUpdateNoteColor(@Color color: Int)

    fun onConvertNote()


    fun getString(@StringRes resId: Int): String

    fun finish()

}