package sgtmelon.scriptum.cleanup.domain.interactor.callback.notification

import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.NotificationInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.NotificationViewModel

/**
 * Interface for communication [NotificationViewModel] with [NotificationInteractor].
 */
interface INotificationInteractor : IParentInteractor {

    suspend fun getCount(): Int

}