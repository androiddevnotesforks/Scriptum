package sgtmelon.scriptum.domain.model.result

import sgtmelon.scriptum.infrastructure.model.key.AppError

/**
 * Class for track processing of backup file export.
 */
sealed class ExportResult {
    data class Success(val path: String) : ExportResult()
    data class Error(val value: AppError) : ExportResult()
}