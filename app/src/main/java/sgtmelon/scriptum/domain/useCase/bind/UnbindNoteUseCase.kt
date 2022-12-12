package sgtmelon.scriptum.domain.useCase.bind

import sgtmelon.scriptum.data.repository.database.BindRepo

class UnbindNoteUseCase(private val repository: BindRepo) {

    suspend operator fun invoke(noteId: Long) = repository.unbindNote(noteId)
}