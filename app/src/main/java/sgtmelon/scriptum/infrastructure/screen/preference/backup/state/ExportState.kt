package sgtmelon.scriptum.infrastructure.screen.preference.backup.state

import sgtmelon.scriptum.infrastructure.model.key.AppError

/**
 * State of data export.
 */
sealed class ExportState {

    object ShowLoading : ExportState()

    object LoadSuccess : ExportState()

    data class LoadError(val value: AppError) : ExportState()

    object Finish : ExportState()
}