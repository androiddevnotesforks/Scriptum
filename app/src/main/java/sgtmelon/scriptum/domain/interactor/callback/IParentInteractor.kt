package sgtmelon.scriptum.domain.interactor.callback

import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor

/**
 * Parent interface for communicate with children of [ParentInteractor].
 */
interface IParentInteractor {
    fun onDestroy(func: () -> Unit = {})
}