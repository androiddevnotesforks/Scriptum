package sgtmelon.scriptum.screen.ui.callback

import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.interactor.SplashInteractor
import sgtmelon.scriptum.screen.ui.SplashActivity

/**
 * Interface for communication [SplashInteractor] with [SplashActivity]
 */
interface ISplashBridge : AlarmControl.Bridge.Cancel, BindControl.NoteBridge.Notify