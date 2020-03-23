package sgtmelon.scriptum.interactor.callback

import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.presentation.control.bind.BindControl

/**
 * Interface for communication with [BindInteractor]
 */
interface IBindInteractor : IParentInteractor {

    suspend fun notifyNoteBind(callback: BindControl.NoteBridge.Notify?)

    suspend fun notifyInfoBind(callback: BindControl.InfoBridge?)

}