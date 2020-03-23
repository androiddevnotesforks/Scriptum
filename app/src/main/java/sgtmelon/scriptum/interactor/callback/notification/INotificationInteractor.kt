package sgtmelon.scriptum.interactor.callback.notification

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.notification.NotificationInteractor

import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.presentation.screen.vm.impl.notification.NotificationViewModel

/**
 * Interface for communication [NotificationViewModel] with [NotificationInteractor]
 */
interface INotificationInteractor : IParentInteractor {

    @Theme val theme: Int

    suspend fun getCount(): Int

    suspend fun getList(): MutableList<NotificationItem>

    suspend fun cancelNotification(item: NotificationItem)

}