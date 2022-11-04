package sgtmelon.scriptum.cleanup.domain.interactor.callback.main

import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.NotesInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesViewModel

/**
 * Interface for communication [NotesViewModel] with [NotesInteractor].
 */
interface INotesInteractor : IParentInteractor {

    suspend fun convertNote(item: NoteItem): NoteItem
}