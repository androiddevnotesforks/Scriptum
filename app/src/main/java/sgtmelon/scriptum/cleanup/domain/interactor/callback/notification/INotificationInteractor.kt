package sgtmelon.scriptum.cleanup.domain.interactor.callback.notification

import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.NotificationInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.INotificationViewModel

/**
 * Interface for communication [INotificationViewModel] with [NotificationInteractor].
 */
interface INotificationInteractor : IParentInteractor {

    suspend fun getCount(): Int

    suspend fun getList(): MutableList<NotificationItem>


    suspend fun setNotification(item: NotificationItem): NotificationItem?

    suspend fun cancelNotification(item: NotificationItem)

}