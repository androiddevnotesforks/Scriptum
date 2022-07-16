package sgtmelon.scriptum.infrastructure.preferences

import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.annotation.Repeat
import sgtmelon.scriptum.cleanup.domain.model.annotation.SavePeriod
import sgtmelon.scriptum.cleanup.domain.model.annotation.Sort
import sgtmelon.scriptum.cleanup.domain.model.annotation.Theme

interface Preferences {

    var isFirstStart: Boolean

    // App settings

    @Theme var theme: Int

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

    @SavePeriod var savePeriod: Int

    // Alarm settings

    @Repeat var repeat: Int

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