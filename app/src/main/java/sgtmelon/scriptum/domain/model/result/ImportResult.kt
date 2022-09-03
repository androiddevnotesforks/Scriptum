package sgtmelon.scriptum.domain.model.result

/**
 * Class for track processing of backup file import.
 */
sealed class ImportResult {
    object Simple : ImportResult()
    data class Skip(val skipCount: Int) : ImportResult()
    object Error : ImportResult()
}