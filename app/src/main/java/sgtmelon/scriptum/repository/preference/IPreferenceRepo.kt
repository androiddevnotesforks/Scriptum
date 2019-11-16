package sgtmelon.scriptum.repository.preference

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.annotation.Theme

/**
 * Interface for communicate with [PreferenceRepo]
 */
interface IPreferenceRepo {

    var firstStart: Boolean

    @Theme var theme: Int


    @Repeat var repeat: Int

    var signal: Int

    var melodyUri: String

    var volume: Int

    var volumeIncrease: Boolean


    @Sort var sort: Int

    @Color var defaultColor: Int

    var pauseSaveOn: Boolean

    var autoSaveOn: Boolean

    var savePeriod: Int


    fun clear()

}