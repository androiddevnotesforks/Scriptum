package sgtmelon.scriptum.interactor.callback

import sgtmelon.scriptum.interactor.BindInteractor

/**
 * Interface for communication with [BindInteractor]
 */
interface IBindInteractor : IParentInteractor {

    fun notifyBind()

}