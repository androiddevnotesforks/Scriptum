package sgtmelon.scriptum.presentation.screen.ui.callback.notification

import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.notification.INotificationViewModel

/**
 * Interface for communication [INotificationViewModel] with [NotificationActivity].
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


    fun showSnackbar(@Theme theme: Int)

    fun notifyList(list: List<NotificationItem>)

    fun setList(list: List<NotificationItem>)

    fun notifyItemRemoved(list: List<NotificationItem>, p: Int)

    fun notifyItemInsertedScroll(list: List<NotificationItem>, p: Int)

}