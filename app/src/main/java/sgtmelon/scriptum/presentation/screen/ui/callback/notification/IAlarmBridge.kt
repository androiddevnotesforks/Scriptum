package sgtmelon.scriptum.presentation.screen.ui.callback.notification

import sgtmelon.scriptum.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity

/**
 * Interface for communication [IAlarmInteractor] with [AlarmActivity].
 */
interface IAlarmBridge : AlarmControl.Bridge.Set, BindControl.InfoBridge