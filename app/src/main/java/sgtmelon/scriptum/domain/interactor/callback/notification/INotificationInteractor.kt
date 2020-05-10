package sgtmelon.scriptum.domain.interactor.callback.notification

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.impl.notification.NotificationInteractor

import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.presentation.screen.vm.impl.notification.NotificationViewModel

/**
 * Interface for communication [NotificationViewModel] with [NotificationInteractor]
 */
interface INotificationInteractor : IParentInteractor {

    @Theme val theme: Int?

    suspend fun getCount(): Int?

    suspend fun getList(): MutableList<NotificationItem>?

    suspend fun cancelNotification(item: NotificationItem)

}