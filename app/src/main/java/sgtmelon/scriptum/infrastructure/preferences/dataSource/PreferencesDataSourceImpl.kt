package sgtmelon.scriptum.infrastructure.preferences.dataSource

import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.annotation.Repeat
import sgtmelon.scriptum.cleanup.domain.model.annotation.SavePeriod
import sgtmelon.scriptum.cleanup.domain.model.annotation.Sort
import sgtmelon.scriptum.cleanup.domain.model.annotation.Theme
import sgtmelon.scriptum.data.dataSource.PreferencesDataSource
import sgtmelon.scriptum.infrastructure.preferences.Preferences

class PreferencesDataSourceImpl(
    private val preferences: Preferences
): PreferencesDataSource {

    override var isFirstStart: Boolean
        get() = preferences.isFirstStart
        set(value) = run { preferences.isFirstStart = value }

    // App settings

    @Theme override var theme: Int
        get() = preferences.theme
        set(value) = run { preferences.theme = value }

    // Backup settings

    override val isBackupSkipImports: Boolean get() = preferences.isBackupSkipImports

    // Note settings

    @Sort override var sort: Int
        get() = preferences.sort
        set(value) = run { preferences.sort = value }

    @Color override var defaultColor: Int
        get() = preferences.defaultColor
        set(value) = run { preferences.defaultColor = value }

    override val isPauseSaveOn: Boolean get() = preferences.isPauseSaveOn

    override val isAutoSaveOn: Boolean get() = preferences.isAutoSaveOn

    @SavePeriod override var savePeriod: Int
        get() = preferences.savePeriod
        set(value) = run { preferences.savePeriod = value }

    // Alarm settings

    @Repeat override var repeat: Int
        get() = preferences.repeat
        set(value) = run { preferences.repeat = value }

    override var signal: String
        get() = preferences.signal
        set(value) = run { preferences.signal = value }

    override var melodyUri: String
        get() = preferences.melodyUri
        set(value) = run { preferences.melodyUri = value }

    override var volume: Int
        get() = preferences.volume
        set(value) = run { preferences.volume = value }

    override val isVolumeIncrease: Boolean get() = preferences.isVolumeIncrease

    // Developer

    override var isDeveloper: Boolean
        get() = preferences.isDeveloper
        set(value) = run { preferences.isDeveloper = value }

    override fun clear() = preferences.clear()
}