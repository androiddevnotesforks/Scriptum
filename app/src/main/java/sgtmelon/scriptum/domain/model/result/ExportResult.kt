package sgtmelon.scriptum.domain.model.result

/**
 * Class for track processing of backup file export.
 */
sealed class ExportResult {
    data class Success(val path: String) : ExportResult()
    object Error : ExportResult()
}