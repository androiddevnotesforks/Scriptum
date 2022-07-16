package sgtmelon.scriptum.infrastructure.preferences

import sgtmelon.scriptum.cleanup.domain.model.annotation.*

/**
 * Interface for communicate with [PreferencesImpl]
 */
interface Preferences {

    var isFirstStart: Boolean

    @Theme var theme: Int


    var isBackupSkipImports: Boolean


    @Sort var sort: Int

    @Color var defaultColor: Int

    var isPauseSaveOn: Boolean

    var isAutoSaveOn: Boolean

    @SavePeriod var savePeriod: Int


    @Repeat var repeat: Int

    var signal: Int

    var melodyUri: String

    var volume: Int

    var isVolumeIncrease: Boolean


    var isDeveloper: Boolean

    fun clear()

}