package sgtmelon.scriptum.data.repository.preference

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme

/**
 * Interface for communicate with [PreferenceRepo]
 */
interface IPreferenceRepo {

    var firstStart: Boolean

    @Theme var theme: Int


    var importSkip: Boolean


    @Sort var sort: Int

    @Color var defaultColor: Int

    var pauseSaveOn: Boolean

    var autoSaveOn: Boolean

    var savePeriod: Int


    @Repeat var repeat: Int

    var signal: Int

    var melodyUri: String

    var volume: Int

    var volumeIncrease: Boolean


    var isDeveloper: Boolean

    fun clear()

}