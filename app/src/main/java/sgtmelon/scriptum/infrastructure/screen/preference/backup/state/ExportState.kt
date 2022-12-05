package sgtmelon.scriptum.infrastructure.screen.preference.backup.state

/**
 * State of data export.
 */
sealed class ExportState {

    object ShowLoading : ExportState()

    object HideLoading : ExportState()

    class LoadSuccess(val path: String) : ExportState()

    object LoadError : ExportState()
}