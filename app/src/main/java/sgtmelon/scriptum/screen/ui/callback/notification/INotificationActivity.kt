package sgtmelon.scriptum.screen.ui.callback.notification

import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import sgtmelon.scriptum.screen.vm.notification.NotificationViewModel

/**
 * Interface for communication [NotificationViewModel] with [NotificationActivity]
 */
interface INotificationActivity : INotificationBridge {

    fun setupToolbar()

    fun setupRecycler(@Theme theme: Int)

    fun showProgress()

    fun onBindingList()

    fun startNoteActivity(item: NotificationItem)


    fun notifyList(list: List<NotificationItem>)

    fun notifyItemRemoved(list: List<NotificationItem>, p: Int)

}