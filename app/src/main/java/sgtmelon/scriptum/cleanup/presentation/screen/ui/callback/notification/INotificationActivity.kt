package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.notification

import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.INotificationViewModel

/**
 * Interface for communication [INotificationViewModel] with [NotificationActivity].
 */
interface INotificationActivity : SystemReceiver.Bridge.Alarm,
    SystemReceiver.Bridge.Bind {

    fun setupToolbar()

    fun setupRecycler()

    fun setupInsets()


    fun prepareForLoad()

    fun showProgress()

    fun hideEmptyInfo()


    fun onBindingList()


    fun showSnackbar()

    fun notifyList(list: List<NotificationItem>)

    fun setList(list: List<NotificationItem>)

    fun notifyItemRemoved(list: List<NotificationItem>, p: Int)

    fun notifyItemInsertedScroll(list: List<NotificationItem>, p: Int)

}