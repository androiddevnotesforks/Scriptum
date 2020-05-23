package sgtmelon.scriptum.domain.interactor.callback.notification

import sgtmelon.scriptum.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.domain.model.item.MelodyItem
import sgtmelon.scriptum.domain.model.state.SignalState

/**
 * Interface for communicate with [SignalInteractor]
 */
interface ISignalInteractor {

    val typeCheck: BooleanArray

    val state: SignalState?


    fun getMelodyUri(melodyList: List<MelodyItem> = this.melodyList): String

    fun setMelodyUri(value: String)

    val melodyCheck: Int

    val melodyList: List<MelodyItem>

}