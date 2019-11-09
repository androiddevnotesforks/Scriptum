package sgtmelon.scriptum.interactor.callback

import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.interactor.BindInteractor

/**
 * Interface for communication with [BindInteractor]
 */
interface IBindInteractor : IParentInteractor {

    fun notifyNoteBind(callback: BindControl.NoteBridge.Notify?)

    fun notifyInfoBind(callback: BindControl.InfoBridge?)

}