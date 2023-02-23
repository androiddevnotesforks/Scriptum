package sgtmelon.scriptum.domain.useCase.note.createNote

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

interface CreateNoteUseCase<T : NoteItem> {

    operator fun invoke(): T

}