package sgtmelon.scriptum.domain.model.result

import sgtmelon.scriptum.domain.interactor.callback.IBackupInteractor

/**
 * Class for result processing of [IBackupInteractor.import].
 */
sealed class ImportResult(val isSuccess: Boolean) {
    class Simple(isSuccess: Boolean) : ImportResult(isSuccess)
    class Skip(isSuccess: Boolean, val skipCount: Int) : ImportResult(isSuccess)
}