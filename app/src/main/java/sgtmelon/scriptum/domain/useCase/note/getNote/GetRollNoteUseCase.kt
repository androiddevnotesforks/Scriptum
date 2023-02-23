package sgtmelon.scriptum.domain.useCase.note.getNote

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.exception.note.IllegalNoteTypeException
import sgtmelon.scriptum.infrastructure.utils.extensions.record

class GetRollNoteUseCase(private val repository: NoteRepo) : GetNoteUseCase<NoteItem.Roll> {

    override suspend operator fun invoke(noteId: Long): NoteItem.Roll? {
        val noteItem = repository.getItem(noteId, isOptimal = false)

        if (noteItem !is NoteItem.Roll) {
            IllegalNoteTypeException(NoteItem.Roll::class, noteItem).record()
            return null
        }

        return noteItem
    }
}