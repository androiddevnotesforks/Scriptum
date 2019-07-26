package sgtmelon.scriptum.repository.preference

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.MelodyItem

/**
 * Interface for communication with [PreferenceRepo]
 *
 * @author SerjantArbuz
 */
interface IPreferenceRepo {

    var firstStart: Boolean

    @Theme var theme: Int


    var repeat: Int

    var signal: Int

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

    val volumeIncrease: Boolean


    @Sort var sort: Int

    @Color var defaultColor: Int

    val pauseSaveOn: Boolean

    val autoSaveOn: Boolean

    var savePeriod: Int

}