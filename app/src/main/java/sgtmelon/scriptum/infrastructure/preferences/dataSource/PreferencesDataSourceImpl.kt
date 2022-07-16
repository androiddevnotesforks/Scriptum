package sgtmelon.scriptum.infrastructure.preferences.dataSource

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

    override var isBackupSkipImports: Boolean
        get() = preferences.isBackupSkipImports
        set(value) = run { preferences.isBackupSkipImports = value }

    // Note settings

    override var sort: Int
        get() = preferences.sort
        set(value) = run { preferences.sort = value }

    override var defaultColor: Int
        get() = preferences.defaultColor
        set(value) = run { preferences.defaultColor = value }

    override var isPauseSaveOn: Boolean
        get() = preferences.isPauseSaveOn
        set(value) = run { preferences.isPauseSaveOn = value }

    override var isAutoSaveOn: Boolean
        get() = preferences.isAutoSaveOn
        set(value) = run { preferences.isAutoSaveOn = value }

    override var savePeriod: Int
        get() = preferences.savePeriod
        set(value) = run { preferences.savePeriod = value }

    // Alarm settings

    override var repeat: Int
        get() = preferences.repeat
        set(value) = run { preferences.repeat = value }

    override var signal: Int
        get() = preferences.signal
        set(value) = run { preferences.signal = value }

    override var melodyUri: String
        get() = preferences.melodyUri
        set(value) = run { preferences.melodyUri = value }

    override var volume: Int
        get() = preferences.volume
        set(value) = run { preferences.volume = value }

    override var isVolumeIncrease: Boolean
        get() = preferences.isVolumeIncrease
        set(value) = run { preferences.isVolumeIncrease = value }

    // Developer

    override var isDeveloper: Boolean
        get() = preferences.isDeveloper
        set(value) = run { preferences.isDeveloper = value }

    override fun clear() = preferences.clear()
}