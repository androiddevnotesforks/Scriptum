package sgtmelon.scriptum.infrastructure.screen.preference.backup

interface BackupPreferenceViewModel {

    fun onPause()

    fun startExport()

    fun startImport()

    fun onResultImport(name: String)

}