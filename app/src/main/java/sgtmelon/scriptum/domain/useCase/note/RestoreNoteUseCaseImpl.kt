package sgtmelon.scriptum.domain.useCase.note

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

class RestoreNoteUseCaseImpl(private val repository: NoteRepo) : RestoreNoteUseCase {

    override suspend operator fun invoke(item: NoteItem) = repository.restoreNote(item)
}