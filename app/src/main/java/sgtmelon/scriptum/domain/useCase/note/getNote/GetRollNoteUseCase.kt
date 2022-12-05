package sgtmelon.scriptum.domain.useCase.note.getNote

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

class GetRollNoteUseCase(private val repository: NoteRepo) {

    suspend operator fun invoke(noteId: Long): NoteItem.Roll? {
        val noteItem = repository.getItem(noteId, isOptimal = false)

        if (noteItem !is NoteItem.Roll) return null

        return noteItem
    }
}