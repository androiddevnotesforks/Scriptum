package sgtmelon.scriptum.domain.model.result

import sgtmelon.scriptum.domain.interactor.callback.preference.IBackupPreferenceInteractor

/**
 * Class for result processing of [IBackupPreferenceInteractor.export].
 */
sealed class ExportResult {
    data class Success(val path: String) : ExportResult()
    object Error : ExportResult()
}