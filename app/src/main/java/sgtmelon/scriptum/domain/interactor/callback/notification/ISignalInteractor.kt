package sgtmelon.scriptum.domain.interactor.callback.notification

import sgtmelon.scriptum.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.domain.model.item.MelodyItem
import sgtmelon.scriptum.domain.model.state.SignalState

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