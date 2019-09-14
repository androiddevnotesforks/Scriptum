package sgtmelon.scriptum.interactor.notification

import sgtmelon.scriptum.control.alarm.callback.AlarmCallback
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem

/**
 * Interface for communicate with [NotificationInteractor]
 */
interface INotificationInteractor {

    @Theme val theme: Int

    fun getList(): MutableList<NotificationItem>

    suspend fun cancelNotification(item: NotificationItem, callback: AlarmCallback.Cancel?)

}