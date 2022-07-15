package sgtmelon.scriptum.cleanup.domain.interactor.callback

import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor

/**
 * Parent interface for communicate with children of [ParentInteractor].
 */
interface IParentInteractor {
    fun onDestroy(func: () -> Unit = {})
}