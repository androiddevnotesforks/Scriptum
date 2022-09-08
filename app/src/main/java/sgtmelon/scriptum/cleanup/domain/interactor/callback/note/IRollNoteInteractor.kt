package sgtmelon.scriptum.cleanup.domain.interactor.callback.note

import sgtmelon.scriptum.cleanup.domain.interactor.impl.note.RollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.IRollNoteViewModel

/**
 * Interface for communication [IRollNoteViewModel] with [RollNoteInteractor].
 */
interface IRollNoteInteractor : IParentNoteInteractor<NoteItem.Roll> {

    suspend fun setVisible(noteItem: NoteItem.Roll)

    suspend fun updateRollCheck(noteItem: NoteItem.Roll, p: Int)
}