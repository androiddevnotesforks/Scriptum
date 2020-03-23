package sgtmelon.scriptum.presentation.screen.ui.callback.notification

import sgtmelon.scriptum.interactor.notification.NotificationInteractor
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity

/**
 * Interface for communication [NotificationInteractor] with [NotificationActivity]
 */
interface INotificationBridge : AlarmControl.Bridge.Cancel, BindControl.InfoBridge