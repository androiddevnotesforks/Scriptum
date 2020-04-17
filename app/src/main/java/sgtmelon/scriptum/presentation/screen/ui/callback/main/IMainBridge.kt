package sgtmelon.scriptum.presentation.screen.ui.callback.main

import sgtmelon.scriptum.domain.interactor.impl.main.MainInteractor
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity

/**
 * Interface for communication [MainInteractor] with [MainActivity]
 */
interface IMainBridge : AlarmControl.Bridge.Full,
        BindControl.NoteBridge.Notify,
        BindControl.InfoBridge