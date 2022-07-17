package sgtmelon.scriptum.cleanup.domain.interactor.callback.notification

import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.infrastructure.model.MelodyItem
import sgtmelon.scriptum.infrastructure.model.state.SignalState

/**
 * Interface for communicate with [SignalInteractor].
 */
interface ISignalInteractor {

    // TODO rename: signalTypeCheck
    val typeCheck: BooleanArray

    // TODO rename: signalState
    val state: SignalState?


    suspend fun getMelodyList(): List<MelodyItem>

    fun resetMelodyList()


    suspend fun getMelodyUri(): String?

    suspend fun setMelodyUri(title: String): String?

    suspend fun getMelodyCheck(): Int?

}