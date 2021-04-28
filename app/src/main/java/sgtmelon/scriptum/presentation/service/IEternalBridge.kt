package sgtmelon.scriptum.presentation.service

import sgtmelon.scriptum.domain.interactor.callback.eternal.IEternalInteractor
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl

/**
 * Interface for communication [IEternalInteractor] with [EternalService]
 */
interface IEternalBridge : AlarmControl.Bridge.Full,
    BindControl.NoteBridge.NotifyAll,
    BindControl.NoteBridge.Cancel,
    BindControl.InfoBridge