package sgtmelon.scriptum.infrastructure.screen.preference.backup.state

sealed class ExportSummaryState {

    object Permission : ExportSummaryState()

    object Empty : ExportSummaryState()
}