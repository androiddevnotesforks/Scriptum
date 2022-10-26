package sgtmelon.scriptum.infrastructure.screen.preference.backup.state

sealed class ImportSummaryState {

    object StartSearch : ImportSummaryState()

    object Permission : ImportSummaryState()

    class Found(val count: Int) : ImportSummaryState()

    object NoFound : ImportSummaryState()

}