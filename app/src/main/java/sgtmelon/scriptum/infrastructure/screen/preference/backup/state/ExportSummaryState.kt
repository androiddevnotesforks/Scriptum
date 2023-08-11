package sgtmelon.scriptum.infrastructure.screen.preference.backup.state

sealed class ExportSummaryState {

    object Permission : ExportSummaryState()

    data class Path(val value: String) : ExportSummaryState()
}