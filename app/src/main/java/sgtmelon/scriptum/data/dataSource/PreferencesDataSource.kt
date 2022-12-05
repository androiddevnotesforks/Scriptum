package sgtmelon.scriptum.data.dataSource

interface PreferencesDataSource {

    var isFirstStart: Boolean

    // App settings

    var theme: Int

    // Backup settings

    val isBackupSkipImports: Boolean

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