package sgtmelon.scriptum.presentation.screen.vm.callback.notification

import sgtmelon.scriptum.presentation.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.notification.NotificationViewModel

/**
 * Interface for communication [INotificationActivity] with [NotificationViewModel].
 */
interface INotificationViewModel : IParentViewModel {

    fun onUpdateData()

    fun onClickNote(p: Int)

    fun onClickCancel(p: Int)


    fun onSnackbarAction()

    fun onSnackbarDismiss()

}