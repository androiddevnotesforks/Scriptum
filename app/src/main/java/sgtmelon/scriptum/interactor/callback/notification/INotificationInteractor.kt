package sgtmelon.scriptum.interactor.callback.notification

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.notification.NotificationInteractor

import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem

/**
 * Interface for communicate with [NotificationInteractor]
 */
interface INotificationInteractor : IParentInteractor {

    @Theme val theme: Int

    fun getList(): MutableList<NotificationItem>

    suspend fun cancelNotification(item: NotificationItem)

}