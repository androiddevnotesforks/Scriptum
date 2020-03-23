package sgtmelon.scriptum.presentation.screen.ui.callback.notification

import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.interactor.notification.NotificationInteractor
import sgtmelon.scriptum.presentation.screen.ui.notification.NotificationActivity

/**
 * Interface for communication [NotificationInteractor] with [NotificationActivity]
 */
interface INotificationBridge : AlarmControl.Bridge.Cancel, BindControl.InfoBridge