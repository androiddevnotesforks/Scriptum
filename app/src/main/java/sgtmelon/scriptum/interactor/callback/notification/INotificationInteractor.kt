package sgtmelon.scriptum.interactor.callback.notification

import sgtmelon.scriptum.control.alarm.AlarmControl

import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem

/**
 * Interface for communicate with [NotificationInteractor]
 */
interface INotificationInteractor {

    @Theme val theme: Int

    fun getList(): MutableList<NotificationItem>

    suspend fun cancelNotification(item: NotificationItem, callback: AlarmControl.Bridge.Cancel?)

}