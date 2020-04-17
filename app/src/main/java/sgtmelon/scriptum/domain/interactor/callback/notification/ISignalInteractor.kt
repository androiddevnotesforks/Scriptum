package sgtmelon.scriptum.domain.interactor.callback.notification

import sgtmelon.scriptum.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.domain.model.item.MelodyItem
import sgtmelon.scriptum.domain.model.state.SignalState

/**
 * Interface for communicate with [SignalInteractor]
 */
interface ISignalInteractor {

    val signalCheck: BooleanArray

    val signalState: SignalState

    fun getSignalSummary(summaryArray: Array<String>): String

    var melodyUri: String

    val melodyCheck: Int

    val melodyList: List<MelodyItem>

}