package sgtmelon.scriptum.domain.useCase.note.getNote

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.exception.note.IllegalNoteTypeException
import sgtmelon.scriptum.infrastructure.utils.extensions.record

class GetTextNoteUseCase(private val repository: NoteRepo) : GetNoteUseCase<NoteItem.Text> {

    override suspend operator fun invoke(noteId: Long): NoteItem.Text? {
        val noteItem = repository.getItem(noteId, isOptimal = false)

        if (noteItem !is NoteItem.Text) {
            IllegalNoteTypeException(NoteItem.Text::class, noteItem).record()
            return null
        }

        return noteItem
    }
}