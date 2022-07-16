package sgtmelon.scriptum.infrastructure.preferences

import sgtmelon.scriptum.cleanup.domain.model.annotation.*

/**
 * Interface for communicate with [PreferencesImpl]
 */
interface Preferences {

    var isFirstStart: Boolean

    @Theme var theme: Int


    var importSkip: Boolean


    @Sort var sort: Int

    @Color var defaultColor: Int

    var pauseSaveOn: Boolean

    var autoSaveOn: Boolean

    @SavePeriod var savePeriod: Int


    @Repeat var repeat: Int

    var signal: Int

    var melodyUri: String

    var volume: Int

    var volumeIncrease: Boolean


    var isDeveloper: Boolean

    fun clear()

}