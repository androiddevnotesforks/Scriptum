package sgtmelon.scriptum.source.ui.screen.preference.backup

interface BackupPreferenceCallback {

    val isExportPermissionGranted: Boolean
    val isImportPermissionGranted: Boolean

    val isExportEnabled: Boolean
    val isImportEnabled: Boolean

}