package sgtmelon.scriptum.repository.preference

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.MelodyItem
import sgtmelon.scriptum.model.state.SignalState

/**
 * Interface for communication with [PreferenceRepo]
 */
interface IPreferenceRepo {

    var firstStart: Boolean

    @Theme var theme: Int


    @Repeat var repeat: Int

    var signal: Int

    val signalCheck: BooleanArray

    val signalState: SignalState

    val signalSummary: String

    /**
     * If melody not init or was delete - set first melody uri from list
     */
    var melodyUri: String

    /**
     * Index of melody uri in [melodyList]
     */
    val melodyCheck: Int

    val melodyList: List<MelodyItem>

    var volume: Int

    var volumeIncrease: Boolean


    @Sort var sort: Int

    @Color var defaultColor: Int

    var pauseSaveOn: Boolean

    var autoSaveOn: Boolean

    var savePeriod: Int

}