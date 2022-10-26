package sgtmelon.scriptum.infrastructure.screen.preference.backup.state

/**
 * State of getting data for import.
 */
sealed class ImportDataState {
    object Empty : ImportDataState()
    class Normal(val titleArray: Array<String>) : ImportDataState()
}