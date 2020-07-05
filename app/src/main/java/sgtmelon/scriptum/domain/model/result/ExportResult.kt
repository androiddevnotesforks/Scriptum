package sgtmelon.scriptum.domain.model.result

import sgtmelon.scriptum.domain.interactor.callback.IBackupInteractor

/**
 * Class for result processing of [IBackupInteractor.export].
 */
sealed class ExportResult {
    class Success(val path: String) : ExportResult()
    object Error : ExportResult()
}