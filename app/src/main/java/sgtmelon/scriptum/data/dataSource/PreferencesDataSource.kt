package sgtmelon.scriptum.data.dataSource

import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.annotation.Repeat
import sgtmelon.scriptum.cleanup.domain.model.annotation.SavePeriod
import sgtmelon.scriptum.cleanup.domain.model.annotation.Sort
import sgtmelon.scriptum.cleanup.domain.model.annotation.Theme

interface PreferencesDataSource {

    var isFirstStart: Boolean

    // App settings

    @Theme var theme: Int

    // Backup settings

    var isBackupSkipImports: Boolean

    // Note settings

    @Sort var sort: Int

    @Color var defaultColor: Int

    var isPauseSaveOn: Boolean

    var isAutoSaveOn: Boolean

    @SavePeriod var savePeriod: Int

    // Alarm settings

    @Repeat var repeat: Int

    var signal: String

    var melodyUri: String

    var volume: Int

    var isVolumeIncrease: Boolean

    // Developer settings

    var isDeveloper: Boolean

    fun clear()
}