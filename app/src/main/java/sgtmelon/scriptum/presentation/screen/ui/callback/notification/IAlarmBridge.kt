package sgtmelon.scriptum.presentation.screen.ui.callback.notification

import sgtmelon.scriptum.domain.interactor.notification.AlarmInteractor
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity

/**
 * Interface for communication [AlarmInteractor] with [AlarmActivity]
 */
interface IAlarmBridge : AlarmControl.Bridge.Set, BindControl.InfoBridge