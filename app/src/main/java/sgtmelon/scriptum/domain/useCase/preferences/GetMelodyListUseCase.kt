package sgtmelon.scriptum.domain.useCase.preferences

import sgtmelon.scriptum.infrastructure.model.item.MelodyItem

interface GetMelodyListUseCase {

    suspend operator fun invoke(): List<MelodyItem>

    fun reset()
}