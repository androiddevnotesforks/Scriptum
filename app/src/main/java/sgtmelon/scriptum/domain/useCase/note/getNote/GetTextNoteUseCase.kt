package sgtmelon.scriptum.domain.useCase.note.getNote

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

class GetTextNoteUseCase(private val repository: NoteRepo) {

    suspend operator fun invoke(noteId: Long): NoteItem.Text? {
        val noteItem = repository.getItem(noteId, isOptimal = false)

        if (noteItem !is NoteItem.Text) return null

        return noteItem
    }
}