package sgtmelon.scriptum.infrastructure.preferences

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

    var sort: Int

    var defaultColor: Int

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

    var volumePercent: Int

    /**
     * Change of this variable happen inside preference.xml screen (or inside UI tests).
     */
    var isVolumeIncrease: Boolean

    // Developer settings

    var isDeveloper: Boolean

    fun clear()
}