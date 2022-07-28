package sgtmelon.scriptum.cleanup.domain.interactor.callback.notification

import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.GetMelodyListUseCaseImpl
import sgtmelon.scriptum.infrastructure.model.MelodyItem

/**
 * Interface for communicate with [GetMelodyListUseCaseImpl].
 */
interface GetMelodyListUseCase {

    suspend fun getMelodyList(): List<MelodyItem>

    fun resetMelodyList()
}