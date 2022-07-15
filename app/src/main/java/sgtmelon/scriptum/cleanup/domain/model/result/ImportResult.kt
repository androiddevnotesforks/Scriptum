package sgtmelon.scriptum.cleanup.domain.model.result

import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IBackupPreferenceInteractor

/**
 * Class for result processing of [IBackupPreferenceInteractor.import].
 */
sealed class ImportResult {
    object Simple : ImportResult()
    data class Skip(val skipCount: Int) : ImportResult()
    object Error : ImportResult()
}