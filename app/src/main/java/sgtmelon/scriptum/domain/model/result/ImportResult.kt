package sgtmelon.scriptum.domain.model.result

import sgtmelon.scriptum.domain.interactor.callback.IBackupPrefInteractor

/**
 * Class for result processing of [IBackupPrefInteractor.import].
 */
sealed class ImportResult {
    object Simple : ImportResult()
    data class Skip(val skipCount: Int) : ImportResult()
    object Error : ImportResult()
}