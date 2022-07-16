package sgtmelon.scriptum.cleanup.domain.interactor.callback.notification

import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.preferences.model.state.SignalState

/**
 * Interface for communicate with [SignalInteractor].
 */
interface ISignalInteractor {

    val typeCheck: BooleanArray

    val state: SignalState?


    suspend fun getMelodyList(): List<MelodyItem>

    fun resetMelodyList()


    suspend fun getMelodyUri(): String?

    suspend fun setMelodyUri(title: String): String?

    suspend fun getMelodyCheck(): Int?

}