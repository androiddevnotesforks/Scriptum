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

    fun bind()

    fun startNoteActivity(notificationItem: NotificationItem)

    fun notifyDataSetChanged(list: MutableList<NotificationItem>)

    fun notifyItemRemoved(p: Int, list: MutableList<NotificationItem>)

}