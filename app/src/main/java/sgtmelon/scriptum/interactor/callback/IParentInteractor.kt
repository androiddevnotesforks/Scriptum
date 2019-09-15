package sgtmelon.scriptum.interactor.callback

import sgtmelon.scriptum.interactor.ParentInteractor

/**
 * Parent interface for communicate with children of [ParentInteractor]
 */
interface IParentInteractor {

    fun onDestroy(func: () -> Unit = {})

}