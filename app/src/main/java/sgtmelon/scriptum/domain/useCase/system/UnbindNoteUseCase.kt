package sgtmelon.scriptum.domain.useCase.system

import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo

class UnbindNoteUseCase(private val repository: BindRepo) {

    suspend operator fun invoke(noteId: Long) = repository.unbindNote(noteId)
}