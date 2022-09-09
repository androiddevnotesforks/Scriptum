package sgtmelon.scriptum.domain.useCase.main

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo

class ClearBinUseCase(private val repository: NoteRepo) {

    suspend operator fun invoke() = repository.clearBin()
}