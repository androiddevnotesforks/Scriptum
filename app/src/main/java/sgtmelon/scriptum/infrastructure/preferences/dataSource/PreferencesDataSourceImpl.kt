package sgtmelon.scriptum.infrastructure.preferences.dataSource

import sgtmelon.scriptum.data.dataSource.PreferencesDataSource
import sgtmelon.scriptum.infrastructure.preferences.Preferences

class PreferencesDataSourceImpl(
    private val preferences: Preferences
) : PreferencesDataSource {

    override var isFirstStart: Boolean
        get() = preferences.isFirstStart
        set(value) {
            preferences.isFirstStart = value
        }

    // App settings

    override var theme: Int
        get() = preferences.theme
        set(value) {
            preferences.theme = value
        }

    // Backup settings

    override val isBackupSkipImports: Boolean get() = preferences.isBackupSkipImports

    // Note settings

    override var sort: Int
        get() = preferences.sort
        set(value) {
            preferences.sort = value
        }

    override var defaultColor: Int
        get() = preferences.defaultColor
        set(value) {
            preferences.defaultColor = value
        }

    override val isPauseSaveOn: Boolean get() = preferences.isPauseSaveOn

    override val isAutoSaveOn: Boolean get() = preferences.isAutoSaveOn

    override var savePeriod: Int
        get() = preferences.savePeriod
        set(value) {
            preferences.savePeriod = value
        }

    // Alarm settings

    override var repeat: Int
        get() = preferences.repeat
        set(value) {
            preferences.repeat = value
        }

    override var signal: String
        get() = preferences.signal
        set(value) {
            preferences.signal = value
        }

    override var melodyUri: String
        get() = preferences.melodyUri
        set(value) {
            preferences.melodyUri = value
        }

    override var volume: Int
        get() = preferences.volume
        set(value) {
            preferences.volume = value
        }

    override val isVolumeIncrease: Boolean get() = preferences.isVolumeIncrease

    // Developer

    override var isDeveloper: Boolean
        get() = preferences.isDeveloper
        set(value) {
            preferences.isDeveloper = value
        }

    override fun clear() = preferences.clear()
}