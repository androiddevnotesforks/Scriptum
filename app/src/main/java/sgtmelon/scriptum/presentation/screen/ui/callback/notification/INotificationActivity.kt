package sgtmelon.scriptum.presentation.screen.ui.callback.notification

import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.notification.NotificationViewModel

/**
 * Interface for communication [NotificationViewModel] with [NotificationActivity]
 */
interface INotificationActivity : INotificationBridge {

    fun setupToolbar()

    fun setupRecycler(@Theme theme: Int)


    /**
     * Calls before load data inside list.
     */
    fun beforeLoad()

    fun showProgress()


    fun onBindingList()

    fun startNoteActivity(item: NotificationItem)


    fun showSnackbar()

    fun notifyList(list: List<NotificationItem>)

    fun notifyItemRemoved(list: List<NotificationItem>, p: Int)

    fun notifyItemInserted(list: List<NotificationItem>, p: Int)

}