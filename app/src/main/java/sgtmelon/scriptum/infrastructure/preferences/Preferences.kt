package sgtmelon.scriptum.infrastructure.preferences

import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.annotation.Sort

// TODO remove annotations
interface Preferences {

    var isFirstStart: Boolean

    // App settings

    var theme: Int

    // Backup settings

    /**
     * Change of this variable happen inside preference.xml screen (or inside UI tests).
     */
    var isBackupSkipImports: Boolean

    // Note settings

    @Sort var sort: Int

    @Color var defaultColor: Int

    /**
     * Change of this variable happen inside preference.xml screen (or inside UI tests).
     */
    var isPauseSaveOn: Boolean

    /**
     * Change of this variable happen inside preference.xml screen (or inside UI tests).
     */
    var isAutoSaveOn: Boolean

    var savePeriod: Int

    // Alarm settings

    var repeat: Int

    var signal: String

    var melodyUri: String

    var volume: Int

    /**
     * Change of this variable happen inside preference.xml screen (or inside UI tests).
     */
    var isVolumeIncrease: Boolean

    // Developer settings

    var isDeveloper: Boolean

    fun clear()
}