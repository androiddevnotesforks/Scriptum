package sgtmelon.scriptum.cleanup.domain.interactor.callback.notification

import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.infrastructure.model.MelodyItem

/**
 * Interface for communicate with [SignalInteractor].
 */
interface ISignalInteractor {

    suspend fun getMelodyList(): List<MelodyItem>

    fun resetMelodyList()
}