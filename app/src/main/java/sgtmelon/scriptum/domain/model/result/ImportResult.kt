package sgtmelon.scriptum.domain.model.result

import sgtmelon.scriptum.infrastructure.model.key.AppError

/**
 * Class for track processing of backup file import.
 */
sealed class ImportResult {
    object Simple : ImportResult()
    data class Skip(val skipCount: Int) : ImportResult()
    data class Error(val value: AppError) : ImportResult()
}