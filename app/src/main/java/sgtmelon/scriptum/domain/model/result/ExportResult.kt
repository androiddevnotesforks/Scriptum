package sgtmelon.scriptum.domain.model.result

import sgtmelon.scriptum.domain.interactor.callback.IBackupPrefInteractor

/**
 * Class for result processing of [IBackupPrefInteractor.export].
 */
sealed class ExportResult {
    data class Success(val path: String) : ExportResult()
    object Error : ExportResult()
}