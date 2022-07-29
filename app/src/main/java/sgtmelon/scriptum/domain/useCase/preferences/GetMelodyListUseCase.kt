package sgtmelon.scriptum.domain.useCase.preferences

import sgtmelon.scriptum.infrastructure.model.MelodyItem

/**
 * Interface for communicate with [GetMelodyListUseCaseImpl].
 */
interface GetMelodyListUseCase {

    suspend operator fun invoke(): List<MelodyItem>

    fun reset()
}