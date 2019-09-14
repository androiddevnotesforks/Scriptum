package sgtmelon.scriptum.interactor.notification.signal

import sgtmelon.scriptum.model.item.MelodyItem
import sgtmelon.scriptum.model.state.SignalState

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