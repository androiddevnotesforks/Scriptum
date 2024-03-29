package sgtmelon.scriptum.data.dataSource

import sgtmelon.scriptum.data.model.PermissionKey

interface PreferencesDataSource {

    var isFirstStart: Boolean

    var showNotificationsHelp: Boolean
    
    fun isPermissionCalled(key: PermissionKey): Boolean

    fun setPermissionCalled(key: PermissionKey)

    // App settings

    var theme: Int

    // Backup settings

    val isBackupSkip: Boolean

    // Note settings

    var sort: Int

    var defaultColor: Int

    val isPauseSaveOn: Boolean

    val isAutoSaveOn: Boolean

    var savePeriod: Int

    // Alarm settings

    var repeat: Int

    var signal: String

    var melodyUri: String

    var volumePercent: Int

    val isVolumeIncrease: Boolean

    // Developer settings

    var isDeveloper: Boolean

    fun clear()
}