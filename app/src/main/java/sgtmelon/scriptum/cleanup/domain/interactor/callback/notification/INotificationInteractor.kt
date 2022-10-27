package sgtmelon.scriptum.cleanup.domain.interactor.callback.notification

import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.NotificationInteractor
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationViewModel

/**
 * Interface for communication [NotificationViewModel] with [NotificationInteractor].
 */
@Deprecated("Don't use it")
interface INotificationInteractor : IParentInteractor {

    suspend fun getCount(): Int

}