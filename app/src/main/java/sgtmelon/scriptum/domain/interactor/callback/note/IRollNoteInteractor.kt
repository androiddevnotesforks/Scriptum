package sgtmelon.scriptum.domain.interactor.callback.note

import sgtmelon.scriptum.domain.interactor.impl.note.RollNoteInteractor
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.vm.callback.note.IRollNoteViewModel

/**
 * Interface for communication [IRollNoteViewModel] with [RollNoteInteractor].
 */
interface IRollNoteInteractor : IParentNoteInteractor<NoteItem.Roll> {

    suspend fun setVisible(noteId: Long, isVisible: Boolean)

    suspend fun getVisible(noteId: Long): Boolean


    suspend fun updateRollCheck(noteItem: NoteItem.Roll, p: Int)

    suspend fun updateRollCheck(noteItem: NoteItem.Roll, isCheck: Boolean)

}