package sgtmelon.scriptum.domain.interactor.callback

import sgtmelon.scriptum.domain.interactor.BindInteractor
import sgtmelon.scriptum.presentation.control.system.BindControl

/**
 * Interface for communication with [BindInteractor]
 */
interface IBindInteractor : IParentInteractor {

    suspend fun notifyNoteBind(callback: BindControl.NoteBridge.Notify?)

    suspend fun notifyInfoBind(callback: BindControl.InfoBridge?)

}