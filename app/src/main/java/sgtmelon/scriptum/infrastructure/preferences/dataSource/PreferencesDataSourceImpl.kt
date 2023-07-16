package sgtmelon.scriptum.infrastructure.preferences.dataSource

import sgtmelon.scriptum.data.dataSource.PreferencesDataSource
import sgtmelon.scriptum.data.model.PermissionKey
import sgtmelon.scriptum.infrastructure.preferences.Preferences

class PreferencesDataSourceImpl(
    private val preferences: Preferences
) : PreferencesDataSource {

    override var isFirstStart: Boolean
        get() = preferences.isFirstStart
        set(value) {
            preferences.isFirstStart = value
        }

    override var showNotificationsHelp: Boolean
        get() = preferences.showNotificationsHelp
        set(value) {
            preferences.showNotificationsHelp = value
        }

    override fun isPermissionCalled(key: PermissionKey): Boolean {
        return preferences.permissionHistory.contains(key.value)
    }

    override fun setPermissionCalled(key: PermissionKey) {
        val history = preferences.permissionHistory.toMutableSet()
        history.add(key.value)
        preferences.permissionHistory = history
    }

    // App settings

    override var theme: Int
        get() = preferences.theme
        set(value) {
            preferences.theme = value
        }

    // Backup settings

    override val isBackupSkip: Boolean get() = preferences.isBackupSkip

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

    override var volumePercent: Int
        get() = preferences.volumePercent
        set(value) {
            preferences.volumePercent = value
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