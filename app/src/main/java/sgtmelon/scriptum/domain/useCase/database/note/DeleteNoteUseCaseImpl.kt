package sgtmelon.scriptum.domain.useCase.database.note

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

class DeleteNoteUseCaseImpl(private val repository: NoteRepo) : DeleteNoteUseCase {

    override suspend operator fun invoke(item: NoteItem) = repository.deleteNote(item)
}