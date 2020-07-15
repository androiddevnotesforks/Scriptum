package sgtmelon.scriptum.presentation.screen.ui.callback.notification

import sgtmelon.scriptum.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity

/**
 * Interface for communication [INotificationInteractor] with [NotificationActivity].
 */
interface INotificationBridge : AlarmControl.Bridge.Full, BindControl.InfoBridge