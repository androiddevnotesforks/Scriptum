package sgtmelon.scriptum.domain.useCase.note.getNote

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

interface GetNoteUseCase<T : NoteItem> {

    suspend operator fun invoke(noteId: Long): T?

}