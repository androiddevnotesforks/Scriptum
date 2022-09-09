package sgtmelon.scriptum.domain.useCase.main

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo

class ClearBinUseCaseImpl(private val repository: NoteRepo) : ClearBinUseCase {

    override suspend operator fun invoke() = repository.clearBin()
}