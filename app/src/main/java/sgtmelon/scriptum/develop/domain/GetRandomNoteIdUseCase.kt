package sgtmelon.scriptum.develop.domain

import sgtmelon.scriptum.develop.data.DevelopRepo

class GetRandomNoteIdUseCase(private val repository: DevelopRepo) {

    suspend operator fun invoke(): Long? = repository.getRandomNoteId()
}