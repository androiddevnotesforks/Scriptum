package sgtmelon.scriptum.domain.model.result

import sgtmelon.scriptum.domain.interactor.callback.IBackupInteractor

/**
 * Class for result processing of [IBackupInteractor.import].
 */
sealed class ImportResult {
    object Simple : ImportResult()
    class Skip(val skipCount: Int) : ImportResult()
    object Error : ImportResult()
}