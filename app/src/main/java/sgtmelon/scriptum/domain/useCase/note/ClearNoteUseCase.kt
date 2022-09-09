package sgtmelon.scriptum.domain.useCase.note

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

class ClearNoteUseCase(private val repository: NoteRepo) {

    suspend operator fun invoke(item: NoteItem) = repository.clearNote(item)
}