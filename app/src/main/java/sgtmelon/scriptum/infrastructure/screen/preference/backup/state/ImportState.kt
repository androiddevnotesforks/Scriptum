package sgtmelon.scriptum.infrastructure.screen.preference.backup.state

import sgtmelon.scriptum.infrastructure.model.key.AppError


/**
 * State of file import.
 */
sealed class ImportState {

    object ShowLoading : ImportState()

    object LoadSuccess : ImportState()

    data class LoadSkip(val count: Int) : ImportState()

    data class LoadError(val value: AppError) : ImportState()

    object Finish : ImportState()
}