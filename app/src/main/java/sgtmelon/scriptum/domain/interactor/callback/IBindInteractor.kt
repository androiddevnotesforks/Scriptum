package sgtmelon.scriptum.domain.interactor.callback

import sgtmelon.scriptum.domain.interactor.impl.BindInteractor
import sgtmelon.scriptum.presentation.control.system.BindControl

/**
 * Interface for communicate with [BindInteractor].
 */
interface IBindInteractor : IParentInteractor {

    suspend fun notifyNoteBind(callback: BindControl.NoteBridge.NotifyAll?)

    suspend fun notifyInfoBind(callback: BindControl.InfoBridge?)

}